package com.icbcintern.prepaycard.mapper;


import com.icbcintern.prepaycard.pojo.User;
import com.icbcintern.prepaycard.pojo.Wallet;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface WalletMapper {
    /**
     * 新增钱包
     */
    @Insert("insert into wallet(wallet_id,balance,type) values (#{walletId},#{balance},#{type})")
    int insertWallet(Wallet wallet);

    @Select("select * from wallet where id=#{id}")
    Wallet getWalletById(Integer id);

    @Select("select * from wallet where wallet_id=#{walletId}")
    Wallet getWalletByWalletId(String walletId);

    @Select("select count(id) from wallet")
    Integer count();

    @Select("select * from wallet")
    List<Wallet> getAll();

    @Select("select * from wallet limit #{start},#{rows}")
    List<Wallet> getByPage(int start,int rows);

    @Update("update wallet set wallet_id=#{walletId},balance=#{balance},type=#{type} where id=#{id}")
    int updateWalletById(Wallet wallet);

    @Update("update wallet set id=#{id},balance=#{balance},type=#{type} where wallet_id=#{walletId}")
    int updateWalletByWalletId(Wallet wallet);

}
