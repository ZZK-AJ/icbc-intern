package com.icbcintern.prepaycard.contract.dao;

import com.icbcintern.prepaycard.contract.pojo.Contract;
import com.icbcintern.prepaycard.contract.pojo.ContractInstance;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

@Mapper
public interface ContractDao {
    @Select("select * from contractInstance where instance_id=#{instanceId}")
    ContractInstance getContractInstanceById(Integer instanceId);

    @Select("select * from contract where contract_id=#{contractId}")
    Contract getContractById(Integer contractId);

    @Insert("insert into contract(path,name,state)" +
            " values (#{path},#{name},#{state})")
    @SelectKey(statement = {"select LAST_INSERT_ID()"}, keyProperty = "contractId", before = false, resultType = Integer.class)
    void insertContract(Contract contract);

    @Insert("insert into contractInstance(merchant_wallet_id,contract_wallet_id,state,discount,gift_balance)" +
            " values (#{merchantWalletId},#{contractWalletId},#{state},#{discount},#{giftBalance})")
    @SelectKey(statement = {"select LAST_INSERT_ID()"}, keyProperty = "instanceId", before = false, resultType = Integer.class)
    void insertContractInstance(ContractInstance contractInstance);
}
