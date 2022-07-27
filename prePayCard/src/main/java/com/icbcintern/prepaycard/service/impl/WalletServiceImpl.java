package com.icbcintern.prepaycard.service.impl;

import com.icbcintern.prepaycard.pojo.Wallet;
import com.icbcintern.prepaycard.service.WalletService;

import java.util.List;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-07-27 10:13
 **/
public class WalletServiceImpl implements WalletService {
    @Override
    public Boolean insertWallet(Wallet wallet) {
        return false;
    }

    @Override
    public Wallet getWalletById(Integer id) {
        return null;
    }

    @Override
    public Wallet getWalletByWalletId(String walletId) {
        return null;
    }

    @Override
    public Integer count() {
        return null;
    }

    @Override
    public List<Wallet> getAll() {
        return null;
    }

    @Override
    public List<Wallet> getByPage(int start, int rows) {
        return null;
    }

    @Override
    public Boolean updateWalletById(Wallet wallet) {
        return false;
    }

    @Override
    public Boolean updateWalletByWalletId(Wallet wallet) {
        return false;
    }

    @Override
    public Boolean transfer(Wallet from, Wallet to, Long money) {
        return false;
    }
}
