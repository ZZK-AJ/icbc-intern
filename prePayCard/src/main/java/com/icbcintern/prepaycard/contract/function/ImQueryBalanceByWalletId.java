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
 * description:通过wallet_id获取钱包信息
 * @author: He Yihui
 * @create: 2022-07-29 14:04
 **/
@Component
public class ImQueryBalanceByWalletId extends ImFunc{
    public static WalletMapper walletMapper;


    @Autowired
    public void setWalletMapper(WalletMapper walletMapper) {
        ImQueryBalanceByWalletId.walletMapper = walletMapper;
    }

    public ImQueryBalanceByWalletId() {
        super.name="im_query_balance_by_wallet_id";
        super.params.add(Type.I32);
        results.add(Type.I32);
    }

    @Override
    public void function(List<Number> argv) {
        StringUtils stringUtils = new StringUtils(arInstance.get());

        int paramPtr = argv.get(0).intValue();

        String param = null;
        try {
            param = stringUtils.getString(paramPtr);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Wallet wallet_ = null;

        if (walletMapper == null) {
            new Exception("walletMapper未初始化,请检查是否运行于spring容器中;或手动调用setWalletMapper方法").printStackTrace();
            return;
        } else {
            wallet_ = walletMapper.getWalletByWalletId(param);
        }

        ObjectMapper jackson = new ObjectMapper();
        String wallet = null;
        try {
            wallet = jackson.writeValueAsString(wallet_);
        } catch (JsonProcessingException e) {
            wallet = "{}";
        }

        int walletPtr = 0;
        try {
            walletPtr = stringUtils.addString(wallet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        argv.set(0, walletPtr);
    }
}

