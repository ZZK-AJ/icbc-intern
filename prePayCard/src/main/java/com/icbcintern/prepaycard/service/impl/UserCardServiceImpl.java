package com.icbcintern.prepaycard.service.impl;

import com.icbcintern.prepaycard.contract.dao.UserCardMapper;
import com.icbcintern.prepaycard.contract.pojo.UserCard;
import com.icbcintern.prepaycard.service.UserCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserCardServiceImpl implements UserCardService {
    @Autowired
    UserCardMapper userCardMapper;

    @Override
    public UserCard getUserCardByCardId(Integer cardId) {

        return userCardMapper.getUserCardByCardId(cardId);
    }
}
