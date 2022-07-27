package com.icbcintern.prepaycard;

import com.icbcintern.prepaycard.pojo.Wallet;
import com.icbcintern.prepaycard.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-07-27 10:49
 **/
@SpringBootTest
public class WalletServiceTests {
    @Autowired
    WalletService walletService;

    @Test
    void contextLoads(){
        System.out.println(walletService.transfer(new Wallet(1, null, null, null), new Wallet(2, null, null, null), 100L));
        System.out.println(walletService.getAll());
    }
}
