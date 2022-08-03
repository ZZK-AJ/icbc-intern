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
    private Integer cardId;
    private Integer payedAmount;
    private Timestamp payedTime;

}
