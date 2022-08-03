package com.icbcintern.prepaycard.service.impl;

import com.icbcintern.prepaycard.mapper.PayedCardMapper;
import com.icbcintern.prepaycard.pojo.PayedCard;
import com.icbcintern.prepaycard.service.PayService;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    PayedCardMapper payedCardMapper;

    /**
     * @param payedCard 购买的预付卡信息
     * @return Result
     */
    @Override
    public Result insertPayCard(PayedCard payedCard) {
        int effectNum = payedCardMapper.insertPayCard(payedCard);
        if (effectNum > 0) {
            // 如果影响行数大于0，那么就是增加成功
            return Result.ok();
        } else {
            throw new RuntimeException("插入信息失败,插入行数有误");
        }
    }

    @Override
    public Result insertUserPayedCard(int userId, int payCardId, String userWalletId) {
        int effectNum = payedCardMapper.insertUserPayedCard(userId, payCardId, userWalletId);
        if (effectNum > 0) {
            // 如果影响行数大于0，那么就是增加成功
            return Result.ok();
        } else {
            throw new RuntimeException("插入信息失败,插入行数有误");
        }
    }

    @Override
    public Result insertMerchantPayedCard(int merchantId, int payCardId) {
        System.out.println("insertMerchantPayedCard ...");
        System.out.println(merchantId);
        System.out.println(payCardId);

        int effectNum = payedCardMapper.insertMerchantPayedCard(merchantId, payCardId);
        if (effectNum > 0) {
            // 如果影响行数大于0，那么就是增加成功
            return Result.ok();
        } else {
            throw new RuntimeException("插入信息失败,插入行数有误");
        }
    }
}


















