package com.icbcintern.prepaycard.service.impl;

import com.icbcintern.prepaycard.mapper.PayedCardMapper;
import com.icbcintern.prepaycard.pojo.PayedCard;
import com.icbcintern.prepaycard.pojo.UserCard;
import com.icbcintern.prepaycard.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    PayedCardMapper payedCardMapper;

    /**
     * @param payedCard 购买的预付卡信息
     * @return Result
     */
    @Override
    public Boolean insertPayCard(PayedCard payedCard) {
        int effectNum = payedCardMapper.insertPayCard(payedCard);
        return effectNum > 0;
    }

    @Override
    public Boolean insertUserPayedCard(int userId, int payCardId, String userWalletId) {
        int effectNum = payedCardMapper.insertUserPayedCard(userId, payCardId, userWalletId);
        return effectNum > 0;
    }

    @Override
    public Boolean insertMerchantPayedCard(int merchantId, int payCardId) {
        int effectNum = payedCardMapper.insertMerchantPayedCard(merchantId, payCardId);
        return effectNum > 0;
    }

    @Override
    public Boolean updatePayCardById(PayedCard payedCard) {
        int effectNum = payedCardMapper.updatePayCardById(payedCard);
        return effectNum > 0;
    }

    @Override
    public List<PayedCard> getAll() {
        return payedCardMapper.getAll();
    }

    public List<PayedCard> getPayedCardByCardId(Integer cardId) {
        return payedCardMapper.getPayedCardByCardId(cardId);
    }

    @Override
    public PayedCard getPayedCardById(Integer id) {
        return payedCardMapper.getPayedCardById(id);
    }

    /**
     * 获取商户被购买的预付卡信息
     *
     * @param merchantId
     * @return
     */
    @Override
    public List<PayedCard> getPayedCardByMerchantId(Integer merchantId) {
        return payedCardMapper.getPayedCardByMerchantId(merchantId);
    }

    @Override
    public PayedCard getPayedCardByInstanceId(Integer instanceId) {
        return payedCardMapper.getPayedCardByInstanceId(instanceId);
    }

    @Override
    public PayedCard getPayedCardByWalletId(Integer walletId) {
        return payedCardMapper.getPayedCardByWalletId(walletId);
    }

    /**
     * 获取用户购买的预付卡id
     *
     * @param userId 用户id
     * @return 用户购买的预付卡关系
     */
    @Override
    public List<UserCard> getUserCardByUserId(Integer userId) {
        return payedCardMapper.getUserCardByUserId(userId);
    }

    @Override
    public UserCard getUserCardByPayedCardId(Integer payedCardId) {
        return payedCardMapper.getUserCardByPayedCardId(payedCardId);
    }

    @Override
    public List<PayedCard> getPayedCardBymMerchantCardStatus(Integer merchantId, String cardStatus) {
        return payedCardMapper.getPayedCardBymMerchantCardStatus(merchantId, cardStatus);
    }

    @Override
    public List<PayedCard> getPayedCardByUserId(Integer userId) {
        return payedCardMapper.getPayedCardByUserId(userId);
    }
}


















