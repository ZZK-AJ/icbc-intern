package com.icbcintern.prepaycard.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayedCard {
    private Integer id; // 用户购买后生成的预付卡id，关联消费者 预付卡用户关系表，关联商户 预付卡商户关系表
    private Integer merchantId; // 商户 id
    private Integer cardId; // 卡种类id，对应 Card 表的 id
    private String walletId; // 冻结的运营方钱包 id
    private Integer cardStatus;  // 预付卡状态，正常 0, 不可用 1, 已退卡 2
    private Integer instanceId;  // 合约实例 id
    private Timestamp payTime;  // 用户购买预付卡时间
    private Timestamp expireTime;  // 预付卡过期时间

    public final static Integer STATUS_TYPE_NORMAL = 0;
    public final static Integer STATUS_TYPE_FORBID = 1;
    public final static Integer STATUS_TYPE_RETURN = 2;
}
