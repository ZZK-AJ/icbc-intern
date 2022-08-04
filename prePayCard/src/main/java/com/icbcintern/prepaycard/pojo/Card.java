package com.icbcintern.prepaycard.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 预付卡类型表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private Integer id;     // 卡种类id
    private Integer reviewId;
    private Integer merchantId;  // 商户id
    private String walletId;   // 商户钱包id
    private String cardName;
    private String cardType;  // 预付卡类型
    private String cardInfo;
    private Long cardAmount;  // 预付卡金额
    private Integer giftAmount;  // 商户预设赠送的金额
    private Integer discountRate;  // 商户预设消费折扣百分比,0-100, 0为免费, 100为原价

    public final static Integer NAME_LIMIT_LENGTH = 40;
    public final static Integer TYPE_LIMIT_LENGTH = 10;
    public final static Integer INFO_LIMIT_LENGTH = 200;

}
