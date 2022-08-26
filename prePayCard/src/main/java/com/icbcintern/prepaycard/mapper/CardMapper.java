package com.icbcintern.prepaycard.mapper;

import com.icbcintern.prepaycard.pojo.Card;
import com.icbcintern.prepaycard.pojo.Review;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CardMapper {

    @Insert("insert into Card(review_id,merchant_id,wallet_id,card_name,card_type,card_info,card_amount,gift_amount,discount_rate, expire_date)" +
            " values (#{reviewId},#{merchantId},#{walletId},#{cardName},#{cardType},#{cardInfo},#{cardAmount},#{giftAmount},#{discountRate}), #(expireDate)")
//    @SelectKey(statement = {"select LAST_INSERT_ID()"}, keyProperty = "id", before = false, resultType = Integer.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
        //新增时将主键返回
    int insertCard(Card card);

    // 插入商户预付卡信息表记录
    @Insert("insert into merchantCard(merchant_id, card_id) values (#{merchantId},#{cardId})")
    int insertMerchantCard(int merchantId, int cardId);

    @Select("select * from Card where id=#{id}")
    Card getCardById(Integer id);

    @Select("select * from Card where merchant_id=#{merchantId}")
    List<Card> getCardByMerchantId(Integer merchantId);

    @Select("select * from Card")
    List<Card> getAll();

    @Update("update Card set " +
            "id=#{id}," +
            "merchant_id=#{merchantId}," +
            "wallet_id=#{walletId}," +
            "card_name=#{cardName}," +
            "card_type=#{cardType}," +
            "card_info=#{cardInfo}," +
            "card_amount=#{cardAmount}," +
            "gift_amount=#{giftAmount}," +
            "discount_rate=#{discountRate}," +
            "where id=#{id}")
    Integer updateCardById(Card card);
}
