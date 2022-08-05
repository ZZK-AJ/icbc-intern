package com.icbcintern.prepaycard.contract.dao;


import com.icbcintern.prepaycard.contract.pojo.UserCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface  UserCardMapper{
    @Select("select * from userCard where card_id=#{cardId}")
    UserCard getUserCardByCardId(int cardId);
}