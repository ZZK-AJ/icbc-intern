package com.icbcintern.prepaycard.contract.function;



import com.icbcintern.prepaycard.contract.dao.WalletsDao;
import org.wasmer.Imports;
import org.wasmer.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-07-21 08:38
 **/
public class ImportFunction {
    public static List<Imports.Spec>  functionList;

    static{
        functionList = new ArrayList<>();

        {
            AtomicReference<Number> arg1r = new AtomicReference<>();
            AtomicReference<Number> ret1r = new AtomicReference<>();
            Imports.Spec func1 = new Imports.Spec("env", "im_query_balance", argv -> {
                arg1r.set(argv.get(0));
                long arg1 = argv.get(0).longValue();
                long ret1 = WalletsDao.QueryBalanceById(arg1);
                argv.set(0, ret1);
                ret1r.set(argv.get(0));
                return argv;
            }, Arrays.asList(Type.I64), Collections.singletonList(Type.I64));


            functionList.add(func1);
        }
        {
            AtomicReference<Number> arg1r = new AtomicReference<>();
            AtomicReference<Number> arg2r = new AtomicReference<>();
            AtomicReference<Number> arg3r = new AtomicReference<>();
            AtomicReference<Number> ret1r = new AtomicReference<>();
            Imports.Spec func1 = new Imports.Spec("env", "im_transfer", argv -> {
                arg1r.set(argv.get(0));
                arg2r.set(argv.get(1));
                arg3r.set(argv.get(2));
                long arg1 = argv.get(0).longValue();
                long arg2 = argv.get(1).longValue();
                long arg3 = argv.get(2).longValue();
                long ret1 = WalletsDao.Transfer(arg1,arg2,arg3);
                argv.set(0, ret1);
                ret1r.set(argv.get(0));
                return argv;
            }, Arrays.asList(Type.I64,Type.I64,Type.I64), Collections.singletonList(Type.I64));


            functionList.add(func1);
        }


    }
}
