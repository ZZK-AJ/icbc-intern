package com.icbcintern.prepaycard.service;

import com.icbcintern.prepaycard.pojo.Card;
import com.icbcintern.prepaycard.utils.Result;

import java.util.List;

public interface CardService {
    Result insertCard(Card Card);

    Result insertMerchantCard(int merchantId, int cardId);

    List<Card> getAll();

    Card getCardById(Integer id);

    List<Card> getCardByMerchantId(Integer merchantId);

    boolean updateCardById(Card Card);

}
