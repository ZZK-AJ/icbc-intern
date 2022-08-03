package com.icbcintern.prepaycard.mapper;

import com.icbcintern.prepaycard.pojo.Card;
import com.icbcintern.prepaycard.pojo.PayedCard;
import com.icbcintern.prepaycard.pojo.Review;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PayedCardMapper {
    @Insert("insert into payedCard(merchant_id,card_id,wallet_id,card_status,instance_id)" +
            " values (#{merchantId},#{cardId},#{walletId},#{cardStatus},#{instanceId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
        //新增时将主键返回
    int insertPayCard(PayedCard payedCard);

    @Insert("insert into userCard(user_id,card_id, user_wallet_id) values (#{userId},#{cardId}),#{userWalletId}")
    int insertUserPayedCard(int userId, int payCardId, String userWalletId);

    @Insert("insert into merchantCard(merchant_id,card_id) values (#{merchantId},#{cardId})")
    int insertMerchantPayedCard(int merchantId, int payCardId);

    @Update("update payedCard set " +
            "id=#{id}," +
            "merchant_id=#{merchantId}," +
            "card_id=#{cardId}," +
            "wallet_id=#{walletId}," +
            "card_status=#{cardStatus}," +
            "instance_id=#{instanceId}," +
            "where id=#{id}")
    Integer updatePayCardById(PayedCard payedCard);

    @Select("select * from payedCard")
    List<PayedCard> getAll();

    @Select("select * from payedCard where card_id=#{cardId}")
    PayedCard getPayedCardByCardId(Integer cardId);

    @Select("select * from payedCard where id=#{id}")
    PayedCard getPayedCardById(Integer id);

    @Select("select * from payedCard where instance_id=#{instanceId}")
    PayedCard getPayedCardByInstanceId(Integer instanceId);

    @Select("select * from payedCard where wallet_id=#{walletId}")
    PayedCard getPayedCardByWalletId(Integer walletId);


}
