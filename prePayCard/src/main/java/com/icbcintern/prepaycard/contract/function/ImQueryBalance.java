package com.icbcintern.prepaycard.contract.function;

import com.icbcintern.prepaycard.contract.dao.WalletsDao;
import org.wasmer.Type;

import java.util.Arrays;
import java.util.List;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-07-29 15:35
 **/
public class ImQueryBalance extends ImFunc{
    public String name = "im_query_balance";

    public ImQueryBalance() {
        super.name = name;
        super.params.addAll(Arrays.asList(Type.I64));
        super.results.addAll(Arrays.asList(Type.I64));
    }

    @Override
    public void function(List<Number> argv) {
        long arg1 = argv.get(0).longValue();
        long ret1 = WalletsDao.QueryBalanceById(arg1);
        argv.set(0, ret1);
    }
}
