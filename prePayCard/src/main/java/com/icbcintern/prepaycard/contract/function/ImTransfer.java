package com.icbcintern.prepaycard.contract.function;

import com.icbcintern.prepaycard.contract.dao.WalletsDao;
import org.wasmer.Type;

import java.util.Arrays;
import java.util.List;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-07-29 21:00
 **/
public class ImTransfer extends ImFunc{

    public ImTransfer() {
        super.name="im_transfer";
        super.params.addAll(Arrays.asList(Type.I64,Type.I64,Type.I64));
        super.results.add(Type.I64);
    }

    @Override
    public void function(List<Number> argv) {

        long arg1 = argv.get(0).longValue();
        long arg2 = argv.get(1).longValue();
        long arg3 = argv.get(2).longValue();
        long ret1 = WalletsDao.Transfer(arg1,arg2,arg3);
        argv.set(0, ret1);
    }
}
