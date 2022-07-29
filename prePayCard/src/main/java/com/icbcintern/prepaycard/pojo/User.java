package com.icbcintern.prepaycard.pojo;

public class User extends Entity {
    private String payPasswd;

    public User() {
    }

    public User(String payPasswd) {
        this.payPasswd = payPasswd;
    }

    public User(Integer id, String name, String loginPasswd, String payPasswd) {
        super(id, name, loginPasswd);
        this.payPasswd = payPasswd;
    }

    public String getPayPasswd() {
        return payPasswd;
    }

    public void setPayPasswd(String payPasswd) {
        this.payPasswd = payPasswd;
    }
}
