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

public class Merchant {
    private Integer id;
    private String merchantName;
    private String merchantInfo;
    private String loginPasswd;

    public Merchant() {
    }

    public Merchant(Integer id, String merchantName, String merchantInfo, String loginPasswd) {
        this.id = id;
        this.merchantName = merchantName;
        this.merchantInfo = merchantInfo;
        this.loginPasswd = loginPasswd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantInfo() {
        return merchantInfo;
    }

    public void setMerchantInfo(String merchantInfo) {
        this.merchantInfo = merchantInfo;
    }

    public String getLoginPasswd() {
        return loginPasswd;
    }

    public void setLoginPasswd(String loginPasswd) {
        this.loginPasswd = loginPasswd;
    }
}
