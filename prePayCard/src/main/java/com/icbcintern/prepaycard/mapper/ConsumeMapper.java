package com.icbcintern.prepaycard.mapper;

import com.icbcintern.prepaycard.pojo.Consume;
import com.icbcintern.prepaycard.pojo.Review;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;

@Mapper
public interface ConsumeMapper {
    /**
     * 消费后插入消费表
     *
     * @param consume
     * @return
     */
    @Insert("insert into consume(payed_card_id,payed_amount,payed_time)" +
            " values (#{payedCardId},#{payedAmount},#{payedTime}")
    @SelectKey(statement = {"select LAST_INSERT_ID()"}, keyProperty = "id", before = false, resultType = Integer.class)
    int insertConsume(Consume consume);

    /**
     * 根据预付卡 id 获取对应预付卡的消费记录
     *
     * @param payedCardId 预付卡 id
     * @return 消费记录
     */
    @Select("select * from consume where payed_card_id=#{payedCardId}")
    List<Consume> getConsumeByPayedCardId(Integer payedCardId);
}
