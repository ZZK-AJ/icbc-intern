package com.icbcintern.prepaycard.contract.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icbcintern.prepaycard.contract.utils.StringUtils;
import com.icbcintern.prepaycard.mapper.CardMapper;
import com.icbcintern.prepaycard.pojo.Card;
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
public class SqlSelectCard extends ImFunc {
    private static CardMapper cardMapper;

    @Autowired
    public void setCardMapper(CardMapper cardMapper) {
        SqlSelectCard.cardMapper = cardMapper;
    }

    public SqlSelectCard() {
        super.name = "sql_select_card";
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
            int cardId = argv.get(0).intValue();
            Card card = cardMapper.getCardById(cardId);
            res = jackson.writeValueAsString(card);
            System.out.println("预付卡种类:"+res);
            resPtr = stringUtils.addString(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        argv.set(0, resPtr);
    }

}
