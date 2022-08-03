package com.icbcintern.prepaycard.service;

import com.icbcintern.prepaycard.pojo.PayedCard;
import com.icbcintern.prepaycard.utils.Result;

public interface PayService {
    Result insertPayCard(PayedCard PayedCard);

    Result insertMerchantPayedCard(int merchantId, int payCardId);

    Result insertUserPayedCard(int userId, int payCardId, String userWalletId);

}
