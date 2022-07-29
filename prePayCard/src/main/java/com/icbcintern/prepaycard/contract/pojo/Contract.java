package com.icbcintern.prepaycard.contract.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-07-28 17:05
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contract {
    private Integer id;
    private Integer merchantId;
    private Integer walletId;
}
