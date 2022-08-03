package com.icbcintern.prepaycard.service.impl;

import com.icbcintern.prepaycard.mapper.CardMapper;
import com.icbcintern.prepaycard.pojo.Card;
import com.icbcintern.prepaycard.service.CardService;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    CardMapper cardMapper;

    @Override
    public Result insertCard(Card card) {
        // todo 相关的错误处理
        int effectNum = cardMapper.insertCard(card);
        if (effectNum > 0) {
            // 如果影响行数大于0，那么就是增加成功
            return Result.ok();
        } else {
            throw new RuntimeException("插入信息失败,插入行数有误");
        }
    }

    @Override
    public Result insertMerchantCard(int merchantId, int cardId) {
        // todo 相关的错误处理
        int effectNum = cardMapper.insertMerchantCard(merchantId, cardId);
        if (effectNum > 0) {
            // 如果影响行数大于0，那么就是增加成功
            return Result.ok();
        } else {
            throw new RuntimeException("插入信息失败,插入行数有误");
        }
    }

    @Override
    public Card getCardById(Integer id) {
        return cardMapper.getCardById(id);
    }

    @Override
    public List<Card> getCardByMerchantId(Integer merchantId) {
        return cardMapper.getCardByMerchantId(merchantId);
    }

    @Override
    public List<Card> getAll() {
        return cardMapper.getAll();
    }

    @Override
    public boolean updateCardById(Card card) {
        int effectNum = cardMapper.updateCardById(card);
        return effectNum > 0;
    }

}
