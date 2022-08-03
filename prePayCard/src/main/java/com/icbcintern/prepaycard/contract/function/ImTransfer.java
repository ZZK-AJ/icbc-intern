package com.icbcintern.prepaycard.contract.function;

import com.icbcintern.prepaycard.contract.dao.WalletsDao;
import com.icbcintern.prepaycard.contract.utils.StringUtils;
import com.icbcintern.prepaycard.mapper.WalletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wasmer.Type;

import java.util.Arrays;
import java.util.List;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-07-29 21:00
 **/
@Component
public class ImTransfer extends ImFunc{
    public static WalletMapper walletMapper;


    @Autowired
    public void setWalletMapper(WalletMapper walletMapper) {
        ImTransfer.walletMapper = walletMapper;
    }

    public ImTransfer() {
        super.name="im_transfer";
        super.params.addAll(Arrays.asList(Type.I32,Type.I32,Type.I64));
        super.results.add(Type.I64);
    }

    @Override
    public void function(List<Number> argv) {
        StringUtils stringUtils = new StringUtils(arInstance.get());
        int from = argv.get(0).intValue();
        int to = argv.get(1).intValue();
        long arg3 = argv.get(2).longValue();
        long from_id;
        long to_id;
        try {
            String from_wallet_id = stringUtils.getString(from);
            String to_wallet_id = stringUtils.getString(to);
            from_id=walletMapper.getWalletByWalletId(from_wallet_id).getId();
            to_id=walletMapper.getWalletByWalletId(to_wallet_id).getId();

        } catch (Exception e) {
            e.printStackTrace();
            argv.set(0,-1);
            return;
        }

        long ret1 = WalletsDao.Transfer(from_id,to_id,arg3);
        argv.set(0, ret1);
    }
}
