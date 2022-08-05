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
public class SqlUpdateInstance extends ImFunc{
    public static ContractDao contractDao;


    @Autowired
    public void setContractDao(ContractDao contractDao) {
        SqlUpdateInstance.contractDao = contractDao;
    }

    public SqlUpdateInstance() {
        super.name="sql_update_instance";
        super.params.add(Type.I32);
        results.add(Type.I32);
    }
    @Override
    public void function(List<Number> argv) {
        ObjectMapper jackson = new ObjectMapper();
        StringUtils stringUtils = new StringUtils(arInstance.get());
        String instanceJson = null;
        try {
            instanceJson = stringUtils.getString(argv.get(0).intValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("更新合约实例:" + instanceJson);
        ContractInstance instance;
        try {
            instance = jackson.readValue(instanceJson, ContractInstance.class);
            System.out.println("反序列化:"+instance);
            contractDao.updateContractInstance(instance);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

}
