package com.icbcintern.prepaycard.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCard {
    private Integer id;
    private Integer userId;
    private Integer cardId;
    private String userWalletId;

}
