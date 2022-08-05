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

    /**
     * 后端调用
     * 用预付卡id 和 合约id 进行签约
     * 合约中通过预付卡id获取【商户钱包id】和【冻结钱包id】
     * 将余额置入钱包中
     * 返回生成的【合约实例id】
     * 后端再将【合约实例id】插入数据库表中。
     */
    @Test
    void signContract() {
        int res = 0;
        try {
            res = contractService.signContract(1, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("实例id:" + res);

    }

    /**
     * 后端调用
     * 消费
     * 通过 合约实例id 和 支付金额 作为参数
     * 返回值为一条json记录
     */
    @Test
    void transfer() {
        try {
            System.out.println(contractService.transfer(11, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void refund() {
        try {
            System.out.println(contractService.refund(11));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}