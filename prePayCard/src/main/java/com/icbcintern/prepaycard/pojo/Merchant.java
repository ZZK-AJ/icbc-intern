package com.icbcintern.prepaycard.pojo;

/**
 * CREATE TABLE IF NOT EXISTS `prePayCard`.`merchant` (
 * `id` INT NOT NULL AUTO_INCREMENT,
 * `merchant_name` VARCHAR(40) NOT NULL COMMENT '商户名',
 * `merchant_info` VARCHAR(400) NOT NULL COMMENT '商户信息',
 * `login_passwd` VARCHAR(40) NOT NULL COMMENT '商户登录密码',
 * PRIMARY KEY (`id`)
 * )ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COMMENT='商户表';
 */

public class Merchant extends Entity {

    private String merchantInfo;

    public Merchant() {
    }

    public Merchant(Integer id, String merchantName, String merchantInfo, String loginPasswd) {

        this.merchantInfo = merchantInfo;
    }


    public String getMerchantInfo() {
        return merchantInfo;
    }

    public void setMerchantInfo(String merchantInfo) {
        this.merchantInfo = merchantInfo;
    }

}
