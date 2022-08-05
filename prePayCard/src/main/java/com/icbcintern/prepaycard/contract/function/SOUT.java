package com.icbcintern.prepaycard.contract.function;

import com.icbcintern.prepaycard.contract.utils.StringUtils;
import org.springframework.stereotype.Component;
import org.wasmer.Type;

import java.util.List;

/**
 * description:通过wallet_id获取钱包信息
 * @author: He Yihui
 * @create: 2022-07-29 14:04
 **/
@Component
public class SOUT extends ImFunc{



    public SOUT() {
        super.name="sout";
        super.params.add(Type.I32);
    }

    @Override
    public void function(List<Number> argv) {
        StringUtils stringUtils = new StringUtils(arInstance.get());

        int paramPtr = argv.get(0).intValue();
        String str = null;
        try {
            str = stringUtils.getString(paramPtr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("rust输出:"+str);

    }
}

