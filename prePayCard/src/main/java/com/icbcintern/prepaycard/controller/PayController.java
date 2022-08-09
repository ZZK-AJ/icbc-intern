package com.icbcintern.prepaycard.controller;

import com.auth0.jwt.interfaces.Claim;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
     */
    @PostMapping("/pay/payedCard/{id}")
    public Result insertPayCard(@RequestHeader("Authorization") String token,
                                @PathVariable("id") int cardTypeId) throws Exception {
        Result result = new Result();
        DecodedJWT jwt = JwtTools.verifyToken(token);   // 解析 token, 获取用户名
        if (jwt == null || StringUtils.isEmpty(jwt.getClaim("userName").asString())) {
            result.setMsg("token 不合法");
            return result;
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
        if (res) {
            Result.ok();
        } else {
            Result.unOk();
            result.setMsg("未成功添加运营方钱包");
            return result;
        }

        PayedCard payCard = new PayedCard();
        payCard.setCardStatus(PayedCard.STATUS_TYPE_NORMAL);  // 设置预付卡状态为正常
        payCard.setCardId(cardType.getId());
        payCard.setMerchantId(cardType.getMerchantId());
        payCard.setWalletId(walletId);

        String userWalletId = userService.getWalletIdByUserId(user.getId());  // 获取 用户钱包关系表 用户钱包id
        payService.insertUserPayedCard(user.getId(), payCard.getId(), userWalletId);  // 写入用户预付卡关系表
        payService.insertMerchantPayedCard(payCard.getMerchantId(), payCard.getId());  // 写入商户预付卡关系表

        // 购卡之后直接扣用户钱包的钱
        Wallet userWallet = walletService.getWalletByWalletId(userWalletId);
        long setUserBalance = userWallet.getBalance() - cardType.getCardAmount();
        userWallet.setBalance(setUserBalance);
        Boolean aBoolean = walletService.updateWalletByWalletId(userWallet);
        if (aBoolean) {
            Result.ok();
        } else {
            Result.unOk();
            result.setMsg("未成功从用户钱包扣款");
            return result;
        }

        // 调用智能合约进行签约 签约之后会有转账到运营方钱包的操作
        payCard.setInstanceId(0);  // 默认先置为 0，签约后返回实际 instance_id
        payService.insertPayCard(payCard);  // 用户购买预付卡之后，写入预付卡表
        int instanceId = contractService.signContract(payCard.getId(), 1);
        payCard.setInstanceId(instanceId);
        payService.updatePayCardById(payCard); // 用户购买预付卡之后，写入预付卡表

        return result;
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
    @GetMapping("/payCard/user/{userId}")
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

}
