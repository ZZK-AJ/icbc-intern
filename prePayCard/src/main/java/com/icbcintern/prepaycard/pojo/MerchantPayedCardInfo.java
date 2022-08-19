package com.icbcintern.prepaycard.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantPayedCardInfo {
    private String userName;    // 购买的用户名
    private Integer cardId;         // 预付卡 id
    private Integer CardTypeId;     // 卡种类 id
    private Integer cardStatus;  // 预付卡状态，正常 0, 不可用 1, 已退卡 2
    private Integer reviewId;
    private Integer merchantId;  // 商户 id
    private String walletId;   // 商户钱包 id
    private String cardName;
    private String cardType;  // 预付卡类型
    private String cardInfo;
    private Long cardAmount;  // 预付卡金额
    private Integer giftAmount;  // 商户预设赠送的金额
    private Integer discountRate;  // 商户预设消费折扣百分比,0-100, 0为免费, 100为原价

}
