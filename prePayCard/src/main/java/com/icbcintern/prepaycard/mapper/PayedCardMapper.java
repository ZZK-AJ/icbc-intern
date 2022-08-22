package com.icbcintern.prepaycard.mapper;

import com.icbcintern.prepaycard.pojo.PayedCard;
import com.icbcintern.prepaycard.pojo.UserCard;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PayedCardMapper {
    @Insert("insert into payedCard(merchant_id,card_id,wallet_id,card_status,instance_id,expire_time),pay_time" +
            " values (#{merchantId},#{cardId},#{walletId},#{cardStatus},#{instanceId},#{expireTime},#{payTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
        //新增时将主键返回
    int insertPayCard(PayedCard payedCard);

    @Insert("insert into userCard(user_id,card_id, user_wallet_id) values (#{userId},#{payCardId},#{userWalletId})")
    int insertUserPayedCard(int userId, int payCardId, String userWalletId);

    @Insert("insert into merchantCard(merchant_id,card_id) values (#{merchantId},#{payCardId})")
    int insertMerchantPayedCard(int merchantId, int payCardId);

    @Update("update payedCard set " +
            "id=#{id}," +
            "merchant_id=#{merchantId}," +
            "card_id=#{cardId}," +
            "wallet_id=#{walletId}," +
            "card_status=#{cardStatus}," +
            "instance_id=#{instanceId} " +
            "where id=#{id}")
    Integer updatePayCardById(PayedCard payedCard);

    @Select("select * from payedCard")
    List<PayedCard> getAll();

    @Select("select * from payedCard where card_id=#{cardId}")
    List<PayedCard> getPayedCardByCardId(Integer cardId);

    @Select("select * from payedCard where merchant_id=#{merchantId}")
    List<PayedCard> getPayedCardByMerchantId(Integer merchantId);

    @Select("select * from payedCard where id=#{id}")
    PayedCard getPayedCardById(Integer id);

    @Select("select * from payedCard where instance_id=#{instanceId}")
    PayedCard getPayedCardByInstanceId(Integer instanceId);

    @Select("select * from payedCard where wallet_id=#{walletId}")
    PayedCard getPayedCardByWalletId(Integer walletId);

    @Select("select * from userCard where user_id=#{userId}")
    List<UserCard> getUserCardByUserId(Integer userId);

    @Select("select * from userCard where card_id=#{payedCardId}")
    UserCard getUserCardByPayedCardId(Integer payedCardId);

    /**
     * 商户查看对应 merchant_id 和卡状态的记录
     */
    @Select("select * from payedCard where merchant_id=#{merchantId} AND card_status=#{cardStatus}")
    List<PayedCard> getPayedCardBymMerchantCardStatus(Integer merchantId, String cardStatus);

    @Select("select * from payedCard where id in (select card_id from userCard where user_id=#{userId})")
    List<PayedCard> getPayedCardByUserId(Integer userId);
}
