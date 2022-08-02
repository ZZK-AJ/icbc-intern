package com.icbcintern.prepaycard.contract.service;

import com.icbcintern.prepaycard.contract.dao.ContractDao;
import com.icbcintern.prepaycard.contract.pojo.Contract;
import com.icbcintern.prepaycard.contract.utils.InstanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wasmer.Instance;

import java.io.File;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-08-01 11:25
 **/
@Service
public class ContractService {
    @Autowired
    ContractDao contractDao;

    public int deploy(Contract contract) {
        contractDao.insertContract(contract);
        return contract.getContractId();
    }


    public int signContract(int payedCardId, int contractId) throws Exception {
        Contract contract = contractDao.getContractById(contractId);
        String path = contract.getPath();
        File file = new File(path);
        String wasmPaths = file.toURI().toString();
        //TODO 加密或校验合约内容
        Instance instance = InstanceUtils.getWasmInstance(wasmPaths);
        Object[] results = instance.exports.
                getFunction("sign_contract").apply(payedCardId);
        Integer instanceId = (Integer) results[0];
        return instanceId;
    }
}