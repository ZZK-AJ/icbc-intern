package com.icbcintern.prepaycard.contract.dao;

import com.icbcintern.prepaycard.contract.pojo.Contract;
import com.icbcintern.prepaycard.contract.pojo.ContractInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ContractDaoTest {

    @Autowired
    ContractDao contractDao;


    @Test
    void getContractInstanceById() {

        System.out.println(contractDao.getContractInstanceById(1));
    }

    @Test
    void getContractPathById() {
        System.out.println(contractDao.getContractInstanceById(1));
    }

    @Test
    void insert() {
        Contract contract = new Contract(0,"E:\\contract\\lib.wasm","lib",0);
        contractDao.insertContract(contract);
        System.out.println(contract.getContractId());
    }

    @Test
    void insertContractInstance() {
        ContractInstance contractInstance = new ContractInstance(0,1,1,0,100,0L);
        contractDao.insertContractInstance(contractInstance);
    }
}