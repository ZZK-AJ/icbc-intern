package com.icbcintern.prepaycard.mapper;

import com.icbcintern.prepaycard.pojo.Merchant;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MerchantMapper {
    /**
     * 添加 merchant 信息
     */
    @Insert("insert into merchant(id,merchant_name,login_passwd, merchant_info) values (#{id},#{userName},#{loginPasswd},#{merchantInfo})")
    int insertMerchant(Merchant merchant);

    /**
     * 根据 ID 查询 merchant 信息
     */
    @Select("select * from merchant where id=#{id}")
    Merchant getMerchantInfoById(int id);

    /**
     * 根据 用户名 查询 merchant 信息
     */
    @Select("select * from merchant where merchant_name=#{merchantName}")
    Merchant getMerchantByName(String merchantName);

    /**
     * 查询所有用户信息
     */
    @Select("select * from merchant")
    List<Merchant> queryMerchantInfo();

    /**
     * 更新用户信息
     */
    @Update("update merchant set id=#{id},merchant_name=#{merchantName},login_passwd=#{loginPasswd},merchant_info=#{merchantInfo} where id=#{id}")
    int updateMerchantById(Merchant merchant);

    /**
     * 删除用户信息
     */
    @Delete("delete from merchant where id=#{id}")
    int deleteMerchantById(int id);
}
