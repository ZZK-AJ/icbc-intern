package com.icbcintern.prepaycard.service;

import com.icbcintern.prepaycard.contract.pojo.UserCard;

public interface UserCardService {

    UserCard getUserCardByCardId(Integer userId);

}
