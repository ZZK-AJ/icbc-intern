package com.icbcintern.prepaycard.contract.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icbcintern.prepaycard.contract.dao.ContractDao;
import com.icbcintern.prepaycard.contract.pojo.ContractInstance;
import com.icbcintern.prepaycard.contract.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wasmer.Type;

import java.util.List;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-08-01 16:54
 **/
@Component
public class SqlInsertContractInstance extends ImFunc{
    public static ContractDao contractDao;


    @Autowired
    public void setContractDao(ContractDao contractDao) {
        SqlInsertContractInstance.contractDao = contractDao;
    }


    public SqlInsertContractInstance() {
        super.name="sql_insert_contract_instance";
        super.params.add(Type.I32);
        //merchant_wallet_id, contract_wallet_id, state, discount_rate, gift_amount, contract_id
        super.results.add(Type.I32);
    }
    @Override
    public void function(List<Number> argv) {
//        int merchantWalletId = argv.get(0).intValue();
//        int contractWalletId = argv.get(1).intValue();
//        int state = argv.get(2).intValue();
//        int discount = argv.get(3).intValue();
//        long giftBalance = argv.get(4).longValue();
//        int contractId = argv.get(5).intValue();
        ObjectMapper jackson = new ObjectMapper();
        StringUtils stringUtils = new StringUtils(arInstance.get());

        int ptr = argv.get(0).intValue();
        String instanceJson = null;
        try {
            instanceJson = stringUtils.getString(ptr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("插入合约实例:" + instanceJson);

        if (contractDao == null) {
            new Exception("contractDao未初始化,请检查是否运行于spring容器中;或手动调用setContractDao方法").printStackTrace();
            return;
        }

        ContractInstance contractInstance = null;
        try {
            contractInstance = jackson.readValue(instanceJson, ContractInstance.class);
            System.out.println("反序列化:"+contractInstance);
            contractInstance.setInstanceId(0);
            contractDao.insertContractInstance(contractInstance);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }


        argv.set(0,contractInstance.getInstanceId());
    }

}
