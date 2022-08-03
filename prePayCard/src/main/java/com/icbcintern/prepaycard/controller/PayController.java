package com.icbcintern.prepaycard.controller;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
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

    /**
     * 用户购买预付卡
     *
     * @param token      token 验证信息
     * @param cardTypeId 预付卡类型 id
     * @return Result
     */
    @PostMapping("/pay/payedCard/{id}")
    public Result insertPayCard(@RequestHeader("Authorization") String token,
                                @PathVariable("id") int cardTypeId) {
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
            result.setCode(1);
            result.setMsg("未成功添加运营方钱包");
            return result;
        }

        PayedCard payCard = new PayedCard();
        payCard.setCardStatus(PayedCard.STATUS_TYPE_NORMAL);  // 设置预付卡状态为正常
        payCard.setPayedCardId(cardType.getId());
        payCard.setMerchantId(cardType.getMerchantId());
        payCard.setWalletId(walletId);

        // todo 调用智能合约 进行签约 获取合约实例 id(instance_id)

        payCard.setInstanceId(666);

        // 用户购买预付卡之后，写入预付卡表
        payService.insertPayCard(payCard);

        // 通过 用户id 查 用户钱包关系表 用户钱包id
        String userWalletId = userService.getWalletIdByUserId(user.getId());
        // 写入用户预付卡关系表
        payService.insertUserPayedCard(user.getId(), payCard.getId(), userWalletId);  // 写入用户预付卡关系表
        // 写入商户预付卡关系表
        payService.insertMerchantPayedCard(payCard.getMerchantId(), payCard.getId());


        // 从用户钱包 转账到 运营方的冻结钱包
        Wallet operatorWallet = walletService.getWalletByWalletId(walletId);


        Wallet userWallet = walletService.getWalletByWalletId(userWalletId);
        long transferMoney = cardType.getCardAmount() + cardType.getGiftAmount();
        walletService.transfer(userWallet, operatorWallet, transferMoney);  // 购卡之后转账到冻结账户

        return Result.ok();
    }

    @GetMapping("/pay/id/{id}")
    public Result getById(@PathVariable("id") int id) {
//        Result result = new Result();
//        PayedCard payedCard = payService.getPayCardById(id);
//        if (payedCard == null) {
//            result.setCode(1);
//            result.setMsg("查询的id不存在");
//        } else {
//            result.setMsg("success");
//            result.setCode(0);
//            result.setData(payedCard);
//        }
//        return result;

        return Result.ok();
    }

    @GetMapping("/pay/listAll")
    public Result getAll() {
//        List<PayedCard> payedCards = payService.getAll();

//        Result result = Result.ok();
//        result.setData(payedCards);
//        return result;
        return Result.ok();
    }

    @GetMapping("/pay/status/{payedStatus}")
    public Result getByPayedStatus(@PathVariable("payedStatus") int payedStatus) {

//        List<PayedCard> payedCards = payService.getPayedByStatus(payStatus);
//        Result result = Result.ok();
//        result.setData(payedCards);
//
//        return result;
        return Result.ok();
    }

    // 获取用户购买的预付卡
    @GetMapping("/pay/user/{userId}")
    public Result getByUserPayed(@PathVariable("userId") int userId) {

//        List<PayedCard> payedCards = payService.getPayedByUserId(userId);
//        Result result = Result.ok();
//        result.setData(payedCards);

        return Result.ok();
    }

    // 获取商户售出的预付卡
    @GetMapping("/pay/merchant/{merchantId}")
    public Result getByMerchantIdPayed(@PathVariable("merchantId") int merchantId) {

//        List<PayedCard> payedCards = payService.getPayedByMerchantId(merchantId);
//        Result result = Result.ok();
//        result.setData(payedCards);

        return Result.ok();
    }
}
