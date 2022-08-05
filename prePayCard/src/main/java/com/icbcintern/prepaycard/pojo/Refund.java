package com.icbcintern.prepaycard.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Refund {
    private Integer payedCardId;
    private String userName;
    private Integer cardId;
    private String cardName;
    private String cardType;
    private String cardInfo;
    private Long cardAmount;
    private Integer giftAmount;
    private Integer discountRate;
}
