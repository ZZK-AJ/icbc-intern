package com.icbcintern.prepaycard.contract.function;

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
public class SqlSelectInstance extends ImFunc{
    public static ContractDao contractDao;


    @Autowired
    public void setContractDao(ContractDao contractDao) {
        SqlSelectInstance.contractDao = contractDao;
    }

    public SqlSelectInstance() {
        super.name="sql_select_instance";
        super.params.add(Type.I32);
        results.add(Type.I32);
    }
    @Override
    public void function(List<Number> argv) {
        StringUtils stringUtils = new StringUtils(arInstance.get());
        ObjectMapper jackson = new ObjectMapper();
        int instanceId = argv.get(0).intValue();
        String res = null;
        int resPtr = 0;
        try {
            ContractInstance contractInstance = contractDao.getContractInstanceById(instanceId);
            res = jackson.writeValueAsString(contractInstance);
            System.out.println("查询合约实例："+res);
            resPtr = stringUtils.addString(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        argv.set(0,resPtr);
    }

}
