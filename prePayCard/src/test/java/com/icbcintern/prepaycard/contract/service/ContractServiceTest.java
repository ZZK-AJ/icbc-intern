package com.icbcintern.prepaycard.contract.service;

import com.icbcintern.prepaycard.contract.pojo.Contract;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ContractServiceTest {

    @Autowired
    ContractService contractService;

    @Test
    void deploy() {
        System.out.println(contractService.deploy(new Contract(0,
                "E:\\project\\icbc-intern\\prePayCard\\src\\main\\resources\\lib.wasm",
                "预付卡", 0)));
    }

    @Test
    void signContract() {
        int res = 0;
        try {
            res = contractService.signContract(0,4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("实例id"+res);

    }
}