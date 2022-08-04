package com.icbcintern.prepaycard.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description:审核表
 * @author: He Yihui
 * @create: 2022-07-27 14:03
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private Integer id;
    private Integer merchantId;
    private String walletId;
    private String cardName;
    private String cardType;
    private String cardInfo;
    private Long cardAmount;
    private Integer reviewStatus;
    private Integer giftAmount;
    private Integer discountRate;

    public final static Integer NAME_LIMIT_LENGTH = 40;
    public final static Integer TYPE_LIMIT_LENGTH = 10;
    public final static Integer INFO_LIMIT_LENGTH = 200;

    public final static Integer STATUS_TYPE_PROCESSING = 0;
    public final static Integer STATUS_TYPE_PASSED = 1;
    public final static Integer STATUS_TYPE_FAILED = 2;
}
