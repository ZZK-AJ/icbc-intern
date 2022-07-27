package com.icbcintern.prepaycard.service;

import com.icbcintern.prepaycard.pojo.Wallet;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface WalletService {

    Boolean insertWallet(Wallet wallet);

    Wallet getWalletById(Integer id);

    Wallet getWalletByWalletId(String walletId);

    Integer count();

    List<Wallet> getAll();

    List<Wallet> getByPage(int start, int rows);

    Boolean updateWalletById(Wallet wallet);

    Boolean updateWalletByWalletId(Wallet wallet);

    Boolean transfer(Wallet from,Wallet to,Long money);
}
