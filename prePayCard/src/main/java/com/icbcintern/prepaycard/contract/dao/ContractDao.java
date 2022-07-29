package com.icbcintern.prepaycard.contract.dao;

import com.icbcintern.prepaycard.contract.pojo.Contract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ContractDao {
    @Select("select id,merchant_id,wallet_id from Card where id=#{id}")
    Contract getContractInstanceById(Integer id);

    @Select("select path from contractPath where id=#{id}")
    String getContractPathById(Integer id);
}
