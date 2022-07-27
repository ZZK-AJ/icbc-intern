package com.icbcintern.prepaycard.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description:钱包/储蓄账户
 * @author: He Yihui
 * @create: 2022-07-27 08:48
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    private Integer id;

    private String walletId;

    private Long balance;

    private Integer type;
}
