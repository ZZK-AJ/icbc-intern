package com.icbcintern.prepaycard.controller;

import com.icbcintern.prepaycard.contract.service.ContractService;
import com.icbcintern.prepaycard.pojo.*;
import com.icbcintern.prepaycard.service.CardService;
import com.icbcintern.prepaycard.service.ConsumeService;
import com.icbcintern.prepaycard.service.PayService;
import com.icbcintern.prepaycard.service.UserService;
import com.icbcintern.prepaycard.utils.Result;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RefundController {
    @Autowired
    ContractService contractService;
    @Autowired
    ConsumeService consumeService;
    @Autowired
    PayService payService;
    @Autowired
    CardService cardService;
    @Autowired
    UserService userService;

    /**
     * 用户退卡
     *
     * @param payed_card_id 预付卡 id
     * @return Result
     */
    @PostMapping("/refund/payedCardId/{payed_card_id}")
    @Transactional
    public Result refund(@PathVariable("payed_card_id") int payed_card_id) throws Exception {
        Result result = new Result();
        // 根据预付卡id 查询预付卡信息
        PayedCard payedCard = payService.getPayedCardById(payed_card_id);
        payedCard.setCardStatus(1);  // 修改卡状态为不可用，即为退卡中
        // 调用合约执行退费
        try {
            contractService.refund(payedCard.getInstanceId());
        } catch (Exception e) {
            return new Result(1, e.toString(), null);
        }
        // 修改对应预付卡id的预付卡状态
        if (payService.updatePayCardById(payedCard)) {
            Result.ok();
            result.setData(payedCard);
        } else {
            Result.unOk();
            result.setMsg("提交退卡失败");
        }
        return result;
    }

    /**
     * 查询商户被退卡的信息
     *
     * @param merchantId 商户信息
     * @return Result
     */
    @GetMapping("/refund/merchant/{merchantId}")
    public Result RefundBymerchantId(@PathVariable("merchantId") Integer merchantId) {
        Result result = new Result();
        List<Refund> refundInfos = new ArrayList<>();
        List<PayedCard> payedCardRefunds = payService.getPayedCardBymMerchantCardStatus(merchantId, "1");  // 对应 merchantId 状态为1(申请退卡) 的预付卡
        if (payedCardRefunds == null) {
            Result.unOk();
            result.setData("查询商户退卡状态的预付卡失败");
            return result;
        }

        for (PayedCard payedCard : payedCardRefunds) {
            Integer card_id = payedCard.getCardId(); // 卡类型id
            Card card = cardService.getCardById(card_id); // 查询预付卡类型信息表，返回预付卡类型信息
            if (card == null) {
                Result.unOk();
                result.setData("查询 预付卡类型 失败");
                return result;
            }
            // 根据 用户购买的预付卡 id 获取用户预付卡记录信息
            UserCard userCard = payService.getUserCardByPayedCardId(payedCard.getId());
            if (userCard == null) {
                Result.unOk();
                result.setData("查询 用户卡关系 失败");
                return result;
            }
            User user = userService.getUserById(userCard.getUserId()); // 根据用户 id 查询 用户名
            if (user == null) {
                Result.unOk();
                result.setData("查询 用户 失败");
                return result;
            }

            Refund refundInfo = new Refund(payedCard.getId(), user.getName(), card.getId(), card.getCardName(), card.getCardType(), card.getCardInfo(), card.getCardAmount(), card.getGiftAmount(), card.getDiscountRate());
            refundInfos.add(refundInfo);
        }
        System.out.println(refundInfos);
        Result.ok();
        result.setData(refundInfos);
        return result;
    }

    /**
     * 商户同意退卡 退卡状态更新为退卡
     *
     * @param payedCardId 预付卡 id
     * @return Result
     */
    @PostMapping("/refund/pass/{payedCardId}")
    public Result refundPass(@PathVariable("payedCardId") int payedCardId) {
        // 根据 预付卡id 更新 payedCard 表状态
        PayedCard payedCardById = payService.getPayedCardById(payedCardId);
        payedCardById.setCardStatus(PayedCard.STATUS_TYPE_RETURN);  // 设置状态为退卡
        if (payService.updatePayCardById(payedCardById)) {
            return Result.setSuccessMsg("更新退卡状态(退卡)成功！", null);
        } else {
            return Result.setFailMsg("更新退卡状态(退卡)失败", null);
        }
    }

    /**
     * 商户拒绝退卡 退卡状态正常使用
     *
     * @param payedCardId 预付卡 id
     * @return Result
     */
    @PostMapping("/refund/reject/{payedCardId}")
    public Result refundReject(@PathVariable("payedCardId") int payedCardId) {
        // 根据 预付卡id 更新 payedCard 表状态
        PayedCard payedCardById = payService.getPayedCardById(payedCardId);
        payedCardById.setCardStatus(PayedCard.STATUS_TYPE_NORMAL);  // 设置状态为正常
        if (payService.updatePayCardById(payedCardById)) {
            return Result.setSuccessMsg("更新退卡状态(正常)成功！", null);
        } else {
            return Result.setFailMsg("更新退卡状态(正常)失败", null);
        }
    }
}
