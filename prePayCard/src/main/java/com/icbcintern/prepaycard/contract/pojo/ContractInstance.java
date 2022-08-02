package com.icbcintern.prepaycard.contract.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-08-01 14:52
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractInstance {
    private Integer instanceId;
    private Integer merchantWalletId;
    private Integer contractWalletId;
    private Integer state;
    private Integer discount;
    private Long giftBalance;
}
