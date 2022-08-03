package com.icbcintern.prepaycard.contract.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icbcintern.prepaycard.contract.utils.StringUtils;
import com.icbcintern.prepaycard.mapper.WalletMapper;
import com.icbcintern.prepaycard.pojo.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wasmer.Type;

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
        super.params.add(Type.I32);
        super.results.add(Type.I32);
        // 0:成功 -1:钱包id不存在 -2:钱包id非初始化
    }
    @Override
    public void function(List<Number> argv) {
        ObjectMapper jackson = new ObjectMapper();
        StringUtils stringUtils = new StringUtils(arInstance.get());

        int ptr = argv.get(0).intValue();
        String walletJson = null;
        try {
            walletJson = stringUtils.getString(ptr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("更新钱包:" + walletJson);

        Wallet wallet = null;
        try {
            wallet = jackson.readValue(walletJson, Wallet.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (walletMapper == null) {
            new Exception("walletMapper未初始化,请检查是否运行于spring容器中;或手动调用setWalletMapper方法").printStackTrace();
            return;
        }
//        Wallet w = walletMapper.getWalletById(wallet.getId());
        Wallet w = walletMapper.getWalletByWalletId(wallet.getWalletId());
        //如果查不到w或是钱包已经存在
        if (w==null){
            argv.set(0,-1);
            return;
        }
        if (w.getBalance()>0L||w.getType()!=0){
            argv.set(0,-2);
            return;
        }
        walletMapper.updateWalletByWalletId(wallet);
        argv.set(0,0);
    }

}
