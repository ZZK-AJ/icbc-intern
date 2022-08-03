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
public class SqlSelectPayedCard extends ImFunc {
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
            //TODO 从数据库中取出
            int payedCardId = argv.get(0).intValue();
            int instanceId = argv.get(1).intValue();
            // payedCard = PayedCardMapper.xxx(payedCardId)
            payedCard payedCard = new payedCard(1,2, "a4dad2dc-0a71-4ef3-89f8-3bae3bcef4e9", 3, 0,"1ef46f98-beac-410e-87c2-18666df15d91",9);
            res = jackson.writeValueAsString(payedCard);
            resPtr = stringUtils.addString(res);
            String res_ = stringUtils.getString(resPtr);
            System.out.println("查询预付卡("+resPtr+"):"+res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        argv.set(0, resPtr);
    }

    @AllArgsConstructor
    @Data
    class payedCard {
        int id;
        int merchantId;
        String walletId;
        int cardId;
        int cardStatus;
        String userWalletId;
        int instanceId;
    }
}
