package com.icbcintern.prepaycard.service;

import com.icbcintern.prepaycard.pojo.PayedCard;
import com.icbcintern.prepaycard.utils.Result;

import java.util.List;

public interface PayService {
    Result insertPayCard(PayedCard PayedCard);

    Result insertMerchantPayedCard(int merchantId, int payCardId);

    Result insertUserPayedCard(int userId, int payCardId, String userWalletId);

    Boolean updatePayCardById(PayedCard payedCard);

    List<PayedCard> getAll();

    PayedCard getPayedCardById(Integer id);

    PayedCard getPayedCardByCardId(Integer cardId);

    PayedCard getPayedCardByInstanceId(Integer instanceId);

    PayedCard getPayedCardByWalletId(Integer walletId);

}
