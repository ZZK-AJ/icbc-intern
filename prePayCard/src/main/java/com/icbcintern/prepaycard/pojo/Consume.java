package com.icbcintern.prepaycard.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consume {
    private Integer id;
    private Integer payedCardId;
    private Long payedAmount;
    private Long discountPrice;
    private Long gift;
    private Long actualPrice;
    private Timestamp payedTime;
    private Integer state;


}
