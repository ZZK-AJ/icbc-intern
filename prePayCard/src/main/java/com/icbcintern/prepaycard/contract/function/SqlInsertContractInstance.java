package com.icbcintern.prepaycard.contract.function;

import com.icbcintern.prepaycard.contract.dao.ContractDao;
import com.icbcintern.prepaycard.contract.pojo.ContractInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wasmer.Type;

import java.util.Arrays;
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
        super.params.addAll(Arrays.asList(Type.I32,Type.I32,Type.I32,Type.I32,Type.I64));
        //merchant_wallet_id, contract_wallet_id, state, discount_rate, gift_amount
        super.results.add(Type.I32);
    }
    @Override
    public void function(List<Number> argv) {
        int merchantWalletId = argv.get(0).intValue();
        int contractWalletId = argv.get(1).intValue();
        int state = argv.get(2).intValue();
        int discount = argv.get(3).intValue();
        long giftBalance = argv.get(4).longValue();

        ContractInstance contractInstance = new ContractInstance(0,merchantWalletId,contractWalletId,state,discount,giftBalance);

        if (contractDao == null) {
            new Exception("contractDao未初始化,请检查是否运行于spring容器中;或手动调用setContractDao方法").printStackTrace();
            return;
        }
        contractDao.insertContractInstance(contractInstance);
        argv.set(0,contractInstance.getInstanceId());
    }

}
