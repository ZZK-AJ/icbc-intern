package com.icbcintern.prepaycard.contract.function;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-07-21 08:38
 **/
@Component
public class ImportFunction {
    public static List<ImFunc>  functionList;

    static{
        functionList = new ArrayList<>();
        {
            ImFunc iqb = new ImQueryBalance();
            ImFunc iqbbw = new ImQueryBalanceByWalletId();
            ImFunc it = new ImTransfer();
            functionList.add(iqb);
            functionList.add(iqbbw);
            functionList.add(it);
            functionList.add(new SqlSelectPayedCard());
            functionList.add(new SqlSelectCard());
            functionList.add(new SqlUpdateWallet());
            functionList.add(new SqlInsertContractInstance());
            functionList.add(new SqlSelectInstance());
            functionList.add(new SqlUpdateInstance());
            functionList.add(new ImQueryWalletIdByCardId());
            functionList.add(new SOUT());
        }
    }
}
