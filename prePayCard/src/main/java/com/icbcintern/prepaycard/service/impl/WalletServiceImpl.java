package com.icbcintern.prepaycard.service.impl;

import com.icbcintern.prepaycard.mapper.WalletMapper;
import com.icbcintern.prepaycard.pojo.Wallet;
import com.icbcintern.prepaycard.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * description:
 *
 * @author: He Yihui
 * @create: 2022-07-27 10:13
 **/
@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    WalletMapper walletMapper;

    @Override
    public Boolean insertWallet(Wallet wallet) {
        String walletId = wallet.getWalletId();
        if (walletId == null || walletId.equals("")) {
            walletId = UUID.randomUUID().toString();
            wallet.setWalletId(walletId);
        }
        Wallet contains = walletMapper.getWalletByWalletId(walletId);
        if (contains != null) {
            return false;
        }
        int effectNum = walletMapper.insertWallet(wallet);

        return effectNum > 0;
    }

    @Override
    public Wallet getWalletById(Integer id) {
        return walletMapper.getWalletById(id);
    }

    @Override
    public Wallet getWalletByWalletId(String walletId) {
        return walletMapper.getWalletByWalletId(walletId);
    }

    @Override
    public Integer count() {
        return walletMapper.count();
    }

    @Override
    public List<Wallet> getAll() {
        return walletMapper.getAll();
    }

    @Override
    public List<Wallet> getByPage(int start, int rows) {
        return walletMapper.getByPage(start, rows);
    }

    @Override
    public Boolean updateWalletById(Wallet wallet) {
        int effectNum = walletMapper.updateWalletById(wallet);
        return effectNum > 0;
    }

    @Override
    public Boolean updateWalletByWalletId(Wallet wallet) {
        int effectNum = walletMapper.updateWalletByWalletId(wallet);
        return effectNum > 0;
    }

    @Override
    @Transactional
    public Boolean transfer(Wallet from, Wallet to, Long money) {
        if (from == null || to == null || money <= 0) {
            return false;
        }

        if (from.getId() == null) {
            if (from.getWalletId() == null) {
                return false;
            } else {
                from = walletMapper.getWalletByWalletId(from.getWalletId());
            }
        } else {
            from = walletMapper.getWalletById(from.getId());
        }

        if (to.getId() == null) {
            if (to.getWalletId() == null) {
                return false;
            } else {
                to = walletMapper.getWalletByWalletId(to.getWalletId());
            }
        } else {
            to = walletMapper.getWalletById(to.getId());
        }

        if (from == null || to == null) {
            return false;
        }

        if (from.getBalance() < money) {
            return false;
        }

        from.setBalance(from.getBalance() - money);
        to.setBalance(to.getBalance() + money);
        walletMapper.updateWalletById(from);
        walletMapper.updateWalletById(to);

        return true;
    }
}
