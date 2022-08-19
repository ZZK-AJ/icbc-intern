package com.icbcintern.prepaycard.mapper;

import com.icbcintern.prepaycard.pojo.Merchant;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MerchantMapper {
    /**
     * 添加 merchant 信息
     */
    @Insert("insert into merchant(merchant_name,login_passwd, merchant_info) values (#{name},#{loginPasswd},#{merchantInfo})")
    int insertMerchant(Merchant merchant);

    /**
     * 根据 ID 查询 merchant 信息
     */
    @Results(id = "merchantNameResultMap", value = {@Result(property = "name", column = "merchant_name")})
    @Select("select * from merchant where id=#{id}")
    Merchant getMerchantInfoById(int id);

    /**
     * 根据 用户名 查询 merchant 信息
     */
    @ResultMap("merchantNameResultMap")
    @Select("select * from merchant where merchant_name=#{name}")
    Merchant getMerchantByName(String merchantName);

    /**
     * 查询所有用户信息
     * todo Merchant 的字段需要和数据库表一一对应
     */
    @ResultMap("merchantNameResultMap")
    @Select("select * from merchant")
    List<Merchant> queryMerchantInfo();

    /**
     * 更新用户信息
     */
    @Update("update merchant set id=#{id},merchant_name=#{name},login_passwd=#{loginPasswd},merchant_info=#{merchantInfo} where id=#{id}")
    int updateMerchantById(Merchant merchant);

    /**
     * 删除用户信息
     */
    @Delete("delete from merchant where id=#{id}")
    int deleteMerchantById(int id);

    /**
     * 增加商户钱包关系表信息
     */
    @Insert("insert into merchantWallet(merchant_id,wallet_id) values (#{merchantId},#{walletId})")
    int insertMerchantWallet(String walletId, int merchantId);

    @Select("select wallet_id from merchantWallet where merchant_id=#{merchantId}")
    String getMerchantWalletByMerchantId(Integer merchantId);
}
