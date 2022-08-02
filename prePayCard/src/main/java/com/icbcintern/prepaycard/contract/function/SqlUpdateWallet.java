package com.icbcintern.prepaycard.contract.function;

import com.icbcintern.prepaycard.mapper.WalletMapper;
import com.icbcintern.prepaycard.pojo.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wasmer.Type;

import java.util.Arrays;
import java.util.List;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-08-01 16:54
 **/


@Component
public class SqlUpdateWallet extends ImFunc{
    public static WalletMapper walletMapper;


    @Autowired
    public void setWalletMapper(WalletMapper walletMapper) {
        SqlUpdateWallet.walletMapper = walletMapper;
    }


    public SqlUpdateWallet() {
        super.name="sql_update_wallet";
        super.params.addAll(Arrays.asList(Type.I32,Type.I64,Type.I32));
        super.results.add(Type.I32);
        // 0:成功 -1:钱包id不存在 -2:钱包id非初始化
    }
    @Override
    public void function(List<Number> argv) {
        int id = argv.get(0).intValue();
        long balance = argv.get(1).longValue();
        int type = argv.get(2).intValue();
        if (walletMapper == null) {
            new Exception("walletMapper未初始化,请检查是否运行于spring容器中;或手动调用setWalletMapper方法").printStackTrace();
            return;
        }
        Wallet w = walletMapper.getWalletById(id);
        //如果查不到w或是钱包已经存在
        if (w==null){
            argv.set(0,-1);
            return;
        }
        if (w.getBalance()>0L||w.getType()!=0){
            argv.set(0,-2);
            return;
        }
        Wallet wallet = new Wallet(id,w.getWalletId(),balance,type);
        walletMapper.updateWalletById(wallet);
        argv.set(0,0);
    }

}
