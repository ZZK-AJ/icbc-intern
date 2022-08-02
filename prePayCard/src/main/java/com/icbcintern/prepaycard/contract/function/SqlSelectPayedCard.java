package com.icbcintern.prepaycard.contract.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icbcintern.prepaycard.contract.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.wasmer.Type;

import java.util.List;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-08-01 16:54
 **/
public class SqlSelectPayedCard extends ImFunc{
    public SqlSelectPayedCard() {
        super.name="sql_select_payed_card";
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
            //TODO 从数据库中取出
            // int payedCardId=argv.get(0).intValue();
            // payedCard = PayedCardMapper.xxx(payedCardId)
            payedCard payedCard = new payedCard(2, 2, 3, 0);
            res = jackson.writeValueAsString(payedCard);
            System.out.println(res);
            resPtr = stringUtils.addString(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        argv.set(0,resPtr);
    }
    @AllArgsConstructor
    @Data
    class payedCard{
        int merchantId;
        int walletId;
        int payedCardId;
        int cardStatus;
    }
}
