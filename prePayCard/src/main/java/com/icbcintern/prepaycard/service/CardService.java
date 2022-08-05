package com.icbcintern.prepaycard.service;

import com.icbcintern.prepaycard.pojo.Card;
import com.icbcintern.prepaycard.utils.Result;

import java.util.List;

public interface CardService {
    Result insertCard(Card Card);

    Result insertMerchantCard(int merchantId, int cardId);

    List<Card> getAll();

    /**
     * 查询预付卡类型信息表，返回预付卡类型信息
     *
     * @param id 预付卡 id
     * @return 卡类型信息
     */
    Card getCardById(Integer id);

    List<Card> getCardByMerchantId(Integer merchantId);

    boolean updateCardById(Card Card);

}
