package com.icbcintern.prepaycard.mapper;

import com.icbcintern.prepaycard.pojo.Review;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReviewMapper {

    @Insert("insert into review(merchant_id,wallet_id,card_name,card_type,card_info,card_amount,review_status,gift_amount,discount_rate, expire_date)" +
            " values (#{merchantId},#{walletId},#{cardName},#{cardType},#{cardInfo},#{cardAmount},#{reviewStatus},#{giftAmount},#{discountRate}, #{expireDate})")
    @SelectKey(statement = {"select LAST_INSERT_ID()"}, keyProperty = "id", before = false, resultType = Integer.class)
    int insertReview(Review review);

    @Select("select * from review where id=#{id}")
    Review getReviewById(Integer id);

    @Select("select * from review where merchant_id=#{merchantId}")
    List<Review> getReviewByMerchantId(Integer merchantId);

    @Select("select * from review where review_status=#{reviewStatus}")
    List<Review> getReviewByStatus(Integer reviewStatus);

    @Select("select * from review")
    List<Review> getAll();

    @Update("update review set " +
            "id=#{id}," +
            "merchant_id=#{merchantId}," +
            "wallet_id=#{walletId}," +
            "card_name=#{cardName}," +
            "card_type=#{cardType}," +
            "card_info=#{cardInfo}," +
            "card_amount=#{cardAmount}," +
            "review_status=#{reviewStatus} " +
            "gift_amount=#{giftAmount} " +
            "discount_rate=#{discountRate} " +
            "where id=#{id}")
    Integer updateReviewById(Review review);

    @Update("update review set review_status=#{reviewStatus} where id=#{id}")
    Integer updateStatusById(Integer id,Integer reviewStatus);
}
