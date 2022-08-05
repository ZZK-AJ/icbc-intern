package com.icbcintern.prepaycard.contract.function;

import com.icbcintern.prepaycard.contract.dao.UserCardMapper;
import com.icbcintern.prepaycard.contract.pojo.UserCard;
import com.icbcintern.prepaycard.contract.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wasmer.Type;

import java.util.List;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-08-03 13:54
 **/
@Component
public class ImQueryWalletIdByCardId extends ImFunc{
    public static UserCardMapper userCardMapper;


    @Autowired
    public void setWalletMapper(UserCardMapper userCardMapper) {
        ImQueryWalletIdByCardId.userCardMapper = userCardMapper;
    }

    public ImQueryWalletIdByCardId() {
        name="im_query_wallet_id_by_card_id";
        params.add(Type.I32);
        results.add(Type.I32);
    }
    @Override
    public void function(List<Number> argv) {
        StringUtils stringUtils = new StringUtils(arInstance.get());
        int cardId = argv.get(0).intValue();
        int ptr = 0;
        try {
            UserCard userCard = userCardMapper.getUserCardByCardId(cardId);
            if (userCard==null){
                throw new Exception("未找到对应cardId");
            }
            String walletId=userCard.getUserWalletId();
            ptr = stringUtils.addString(walletId);
            System.out.println("钱包id:("+ptr+")"+walletId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        argv.set(0, ptr);
    }


}
