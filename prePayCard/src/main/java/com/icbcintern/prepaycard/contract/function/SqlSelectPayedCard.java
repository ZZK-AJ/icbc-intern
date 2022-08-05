package com.icbcintern.prepaycard.contract.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icbcintern.prepaycard.contract.utils.StringUtils;
import com.icbcintern.prepaycard.mapper.PayedCardMapper;
import com.icbcintern.prepaycard.pojo.PayedCard;
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
public class SqlSelectPayedCard extends ImFunc {
    private static PayedCardMapper payedCardMapper;

    @Autowired
    public void setPayedCardMapper(PayedCardMapper payedCardMapper) {
        SqlSelectPayedCard.payedCardMapper = payedCardMapper;
    }

    public SqlSelectPayedCard() {
        super.name = "sql_select_payed_card";
        super.params.add(Type.I32);
        super.params.add(Type.I32);
        results.add(Type.I32);
    }

    @Override
    public void function(List<Number> argv) {
        StringUtils stringUtils = new StringUtils(arInstance.get());
        ObjectMapper jackson = new ObjectMapper();
        String res = null;
        int resPtr = 0;
        try {
            int payedCardId = argv.get(0).intValue();
            int instanceId = argv.get(1).intValue();
            PayedCard payedCard = null;
            if (payedCardId != 0) {
                payedCard = payedCardMapper.getPayedCardById(payedCardId);
            }else{
                payedCard = payedCardMapper.getPayedCardByInstanceId(instanceId);
            }

            res = jackson.writeValueAsString(payedCard);
            resPtr = stringUtils.addString(res);
            String res_ = stringUtils.getString(resPtr);
            System.out.println("查询预付卡(" + resPtr + "):" + res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        argv.set(0, resPtr);
    }
}
