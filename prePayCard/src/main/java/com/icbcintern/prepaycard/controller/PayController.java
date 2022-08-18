package com.icbcintern.prepaycard.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.icbcintern.prepaycard.contract.service.ContractService;
import com.icbcintern.prepaycard.pojo.*;
import com.icbcintern.prepaycard.service.CardService;
import com.icbcintern.prepaycard.service.PayService;
import com.icbcintern.prepaycard.service.UserService;
import com.icbcintern.prepaycard.service.WalletService;
import com.icbcintern.prepaycard.utils.JwtTools;
import com.icbcintern.prepaycard.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PayController {
    @Autowired
    CardService cardService;
    @Autowired
    PayService payService;
    @Autowired
    UserService userService;
    @Autowired
    WalletService walletService;
    @Autowired
    ContractService contractService;

    /**
     * 用户购买预付卡
     * @param token      token 验证信息
     * @param cardTypeId 预付卡类型 id
     * @return Result
     * TODO 将代码封装到service层或整个trycatch整个包裹,而不是出现错误手动回滚
     */
    @PostMapping("/pay/payedCard/{id}")
    @Transactional
    public Result insertPayCard(@RequestHeader("Authorization") String token,
                                @PathVariable("id") int cardTypeId) throws Exception {
        Result result = new Result();
        DecodedJWT jwt = JwtTools.verifyToken(token);   // 解析 token, 获取用户名
        if (jwt == null || StringUtils.isEmpty(jwt.getClaim("userName").asString())) {
            return Result.setFailMsg("token 不合法", null);
        }
        String userName = jwt.getClaim("userName").asString();
        User user = userService.getUserByUserName(userName);  // 获取用户信息
        Card cardType = cardService.getCardById(cardTypeId); // 通过卡类型 id 查询对应预付卡类型的信息
//      生成运营方钱包表记录,设置初始运营方钱包为冻结，金额为 0；运营方钱包和预付卡进行关联；
        Wallet wallet = new Wallet();
        String walletId = UUID.randomUUID().toString();  // 运营方钱包 id，购买一张预付卡生成一个运营方
        wallet.setWalletId(walletId);
        wallet.setType(0);   // 钱包类型，用户 1, 商户 2, 运营方 3, 冻结 0
        wallet.setBalance((long) 0); // 初始化默认设为 0
        Boolean res = walletService.insertWallet(wallet);
        if (!res) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.setFailMsg("未成功添加运营方钱包", null);
        }

        PayedCard payCard = new PayedCard();
        payCard.setCardStatus(PayedCard.STATUS_TYPE_NORMAL);  // 设置预付卡状态为正常
        payCard.setCardId(cardType.getId());
        payCard.setMerchantId(cardType.getMerchantId());
        payCard.setWalletId(walletId);

        // 先插入钱包
        // 调用智能合约进行签约 签约之后会有转账到运营方钱包的操作
        payCard.setInstanceId(0);  // 默认先置为 0，签约后返回实际 instance_id
        Boolean insertPayCardResult = payService.insertPayCard(payCard);  // 用户购买预付卡之后，写入预付卡表
        if (!insertPayCardResult) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.setFailMsg("写入预付卡表失败", null);
        }
        String userWalletId = userService.getWalletIdByUserId(user.getId());  // 获取 用户钱包关系表 用户钱包id
        payService.insertUserPayedCard(user.getId(), payCard.getId(), userWalletId);  // 写入用户预付卡关系表
        payService.insertMerchantPayedCard(payCard.getMerchantId(), payCard.getId());  // 写入商户预付卡关系表

        // 购卡之后直接扣用户钱包的钱
        Wallet userWallet = walletService.getWalletByWalletId(userWalletId);
        long setUserBalance = userWallet.getBalance() - cardType.getCardAmount();
        if (setUserBalance < 0) {

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.setFailMsg("用户余额不足", null);

        }
        userWallet.setBalance(setUserBalance);
        Boolean aBoolean = walletService.updateWalletByWalletId(userWallet);
        if (!aBoolean) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.setFailMsg("未成功从用户钱包扣款", null);
        }


        int instanceId = contractService.signContract(payCard.getId(), 1);
        payCard.setInstanceId(instanceId);
        Boolean updatePayCardById = payService.updatePayCardById(payCard); // 用户购买预付卡之后，写入预付卡表
        if (!updatePayCardById) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.setFailMsg("更新预付卡表失败", null);
        }

        return Result.setSuccessMsg("购买预付卡成功", null);
    }


    @GetMapping("/pay/id/{id}")
    public Result getById(@PathVariable("id") int id) {
        Result result = new Result();
        PayedCard payedCard = payService.getPayedCardById(id);
        if (payedCard == null) {
            result.setCode(1);
            result.setMsg("查询 id 的预付卡不存在");
        } else {
            Result.ok();
            result.setData(payedCard);
        }
        return result;
    }

    @GetMapping("/pay/listAll")
    public Result getAll() {
        Result result = new Result();
        List<PayedCard> payedCards = payService.getAll();
        if (payedCards == null) {
            result.setCode(1);
            result.setMsg("查询 id 的预付卡不存在");
        } else {
            Result.ok();
            result.setData(payedCards);
        }
        return result;
    }

    // 获取用户购买的预付卡
    @GetMapping("/pay/user/{userId}")
    public Result getByUserPayed(@PathVariable("userId") int userId) {

        return Result.ok();
    }

    // 获取对应钱包 id 的预付卡
    @GetMapping("/pay/wallet/{walletId}")
    public Result getPayedCardByWalletId(@PathVariable("walletId") int walletId) {
        Result result = new Result();
        PayedCard payedCard = payService.getPayedCardByWalletId(walletId);
        if (payedCard == null) {
            result.setCode(1);
            result.setMsg("查询钱包 id 的预付卡不存在");
        } else {
            Result.ok();
            result.setData(payedCard);
        }
        return result;
    }

    /**
     * 用户获取购买的预付卡信息
     *
     * @param userId 用户id
     * @return Result
     */
    @GetMapping("/userCard/user/{userId}")
    public Result getUserCardByUserId(@PathVariable("userId") int userId) {
        Result result = new Result();
        List<UserCard> userCards = payService.getUserCardByUserId(userId);
        if (userCards == null) {
            result.setCode(1);
            result.setMsg("查询用户未购买预付卡");
        } else {
            Result.ok();
            result.setData(userCards);
        }
        return result;
    }

    @GetMapping("/payCard/user/{userId}")
    public Result getPayedCardByUserId(@PathVariable("userId") int userId) {
        Result result = new Result();
        List<PayedCard> cards = payService.getPayedCardByUserId(userId);
        if (cards == null) {
            result.setCode(1);
            result.setMsg("查询用户未购买预付卡");
        } else {
            Result.ok();
            result.setData(cards);
        }
        return result;
    }

    @GetMapping("/payCard/balance/{payCardId}")
    public Result getBalanceByPayCardId(@PathVariable("payCardId") int payCardId) {
        PayedCard payedCard = payService.getPayedCardById(payCardId);
        Integer instanceId = payedCard.getInstanceId();
        Long balance = contractService.getBalance(instanceId);
        Long giftBalance = contractService.getGiftBalance(instanceId);
        if (balance>=0){
            return Result.setSuccessMsg("success",new long[]{balance,giftBalance});
        }
        return Result.setFailMsg("fail",null);
    }


}
