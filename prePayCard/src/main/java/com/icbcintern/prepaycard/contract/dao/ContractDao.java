package com.icbcintern.prepaycard.contract.dao;

import com.icbcintern.prepaycard.contract.pojo.Contract;
import com.icbcintern.prepaycard.contract.pojo.ContractInstance;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ContractDao {
    @Select("select * from contractInstance where instance_id=#{instanceId}")
    ContractInstance getContractInstanceById(Integer instanceId);

    @Select("select * from contract where contract_id=#{contractId}")
    Contract getContractById(Integer contractId);

    @Select("select * from contract")
    List<Contract> getAllContract();
    @Select("select * from contract where state=0")
    List<Contract> getValidContract();

    @Select("select * from contractInstance")
    List<ContractInstance> getAllContractInstance();

    @Insert("insert into contract(path,name,state)" +
            " values (#{path},#{name},#{state})")
    @SelectKey(statement = {"select LAST_INSERT_ID()"}, keyProperty = "contractId", before = false, resultType = Integer.class)
    void insertContract(Contract contract);

    @Insert("insert into contractInstance(merchant_wallet_id,contract_wallet_id,state,discount,gift_balance,contract_id)" +
            " values (#{merchantWalletId},#{contractWalletId},#{state},#{discount},#{giftBalance},#{contractId})")
    @SelectKey(statement = {"select LAST_INSERT_ID()"}, keyProperty = "in" +
            "stanceId", before = false, resultType = Integer.class)
    void insertContractInstance(ContractInstance contractInstance);

    @Update("update contract set " +
            "path=#{path}," +
            "name=#{name}," +
            "state=#{state} " +
            "where contract_id=#{contractId}")
    Integer updateContract(Contract contract);

    @Update("update contractInstance set " +
            "merchant_wallet_id=#{merchantWalletId}," +
            "contract_wallet_id=#{contractWalletId}," +
            "state=#{state}," +
            "discount=#{discount}," +
            "gift_balance=#{giftBalance} " +
            "where instance_id=#{instanceId}")
    int updateContractInstance(ContractInstance contractInstance);
}
