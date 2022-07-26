package com.icbcintern.prepaycard.pojo;

/**
 *
 CREATE TABLE IF NOT EXISTS `prePayCard`.`user` (
 `id` INT NOT NULL AUTO_INCREMENT,
 `user_name` VARCHAR(40) NOT NULL COMMENT '用户名',
 `login_passwd` VARCHAR(40) NOT NULL COMMENT '用户登录密码',
 `pay_passwd`  VARCHAR(40) NOT NULL COMMENT '用户支付密码',
 PRIMARY KEY (`id`)
 )ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COMMENT='用户表';
 */

public class User {
    private Integer id;
    private String userName;
    private String loginPasswd;
    private String payPasswd;

    public User() {
    }

    public User(Integer id, String userName, String loginPasswd, String payPasswd) {
        this.id = id;
        this.userName = userName;
        this.loginPasswd = loginPasswd;
        this.payPasswd = payPasswd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginPasswd() {
        return loginPasswd;
    }

    public void setLoginPasswd(String loginPasswd) {
        this.loginPasswd = loginPasswd;
    }

    public String getPayPasswd() {
        return payPasswd;
    }

    public void setPayPasswd(String payPasswd) {
        this.payPasswd = payPasswd;
    }
}
