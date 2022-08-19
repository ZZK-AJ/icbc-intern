package com.icbcintern.prepaycard.service;

import com.icbcintern.prepaycard.pojo.PayedCard;
import com.icbcintern.prepaycard.pojo.UserCard;

import java.util.List;

public interface PayService {
    Boolean insertPayCard(PayedCard PayedCard);

    /**
     * 购买预付卡之后，写入用户预付卡关系表
     *
     * @param userId       用户 id
     * @param payCardId    预付卡id
     * @param userWalletId 用户钱包 id
     * @return Boolean
     */
    Boolean insertUserPayedCard(int userId, int payCardId, String userWalletId);

    /**
     * 购买预付卡之后，写入商户预付卡关系表
     *
     * @param merchantId 商户 id
     * @param payCardId  预付卡 id
     * @return Boolean
     */
    Boolean insertMerchantPayedCard(int merchantId, int payCardId);

    Boolean updatePayCardById(PayedCard payedCard);

    List<PayedCard> getAll();

    PayedCard getPayedCardById(Integer id);

    /**
     * 获取商户被购买的预付卡信息
     *
     * @param merchantId
     * @return
     */
    List<PayedCard> getPayedCardByMerchantId(Integer merchantId);

    List<PayedCard> getPayedCardByCardId(Integer cardId);

    PayedCard getPayedCardByInstanceId(Integer instanceId);

    PayedCard getPayedCardByWalletId(Integer walletId);

    /**
     * 获取用户购买的预付卡id
     *
     * @param userId 用户id
     * @return 用户购买的预付卡关系
     */
    List<UserCard> getUserCardByUserId(Integer userId);

    /**
     * 通过预付卡 id 获取用户预付卡关联信息
     *
     * @param payedCardId 预付卡 id
     * @return 用户预付卡关联信息
     */
    UserCard getUserCardByPayedCardId(Integer payedCardId);

    /**
     * 商户查看对应 merchant_id 和卡状态的记录
     *
     * @param merchantId 商户 id
     * @param cardStatus 卡状态 id
     * @return 已购预付卡的信息
     */
    List<PayedCard> getPayedCardBymMerchantCardStatus(Integer merchantId, String cardStatus);

    List<PayedCard> getPayedCardByUserId(Integer userId);
}
