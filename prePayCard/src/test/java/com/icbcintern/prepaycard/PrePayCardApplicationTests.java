package com.icbcintern.prepaycard;

import com.icbcintern.prepaycard.mapper.WalletMapper;
import com.icbcintern.prepaycard.pojo.Wallet;
import com.icbcintern.prepaycard.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
class PrePayCardApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    WalletMapper walletMapper;
    @Test
    void walletMapperTest() {
        Integer total = walletMapper.count();
        System.out.println("记录共:" + total + "条");
        System.out.println(walletMapper.getAll());
        Wallet wallet = walletMapper.getWalletById(1);
        Wallet wallet2 = walletMapper.getWalletByWalletId(wallet.getWalletId());
        assert wallet.equals(wallet2);

        String uuid = UUID.randomUUID().toString();
        Wallet newWallet = new Wallet(0, uuid, 10000L, 1);
        walletMapper.insertWallet(newWallet);
        newWallet = walletMapper.getWalletByWalletId(uuid);
        newWallet.setBalance(newWallet.getBalance()+10000L);
        walletMapper.updateWalletByWalletId(newWallet);
        total = walletMapper.count();
        System.out.println("最后2条记录:\n"+walletMapper.getByPage(total - 2, 2));

    }

    @Autowired
    UserService userService;
    @Test
    void userServiceTest(){
        System.out.println(userService.queryUserInfo());
    }
}
