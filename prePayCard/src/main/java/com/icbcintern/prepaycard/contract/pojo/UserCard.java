package com.icbcintern.prepaycard.contract.pojo;

import lombok.Data;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-08-03 15:12
 **/
@Data
public class UserCard {
    int id;
    int userId;
    int cardId;
    String userWalletId;
}
