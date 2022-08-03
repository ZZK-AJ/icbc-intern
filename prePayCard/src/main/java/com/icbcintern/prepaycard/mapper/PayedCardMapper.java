package com.icbcintern.prepaycard.mapper;

import com.icbcintern.prepaycard.pojo.Card;
import com.icbcintern.prepaycard.pojo.PayedCard;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface PayedCardMapper {
    @Insert("insert into payedCard(merchant_id,payed_card_id,wallet_id,card_status,instance_id)" +
            " values (#{merchantId},#{payedCardId},#{walletId},#{cardStatus},#{instanceId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
        //新增时将主键返回
    int insertPayCard(PayedCard payedCard);

    @Insert("insert into userCard(user_id,card_id, user_wallet_id) values (#{userId},#{cardId}),#{userWalletId}")
    int insertUserPayedCard(int userId, int payCardId, String userWalletId);

    @Insert("insert into merchantCard(merchant_id,card_id) values (#{merchantId},#{cardId})")
    int insertMerchantPayedCard(int merchantId, int payCardId);

}
