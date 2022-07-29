package com.icbcintern.prepaycard.pojo;

public class Entity {
    protected Integer id;
    protected String name;
    protected String loginPasswd;

    public Entity() {
    }

    public Entity(Integer id, String name, String loginPasswd) {
        this.id = id;
        this.name = name;
        this.loginPasswd = loginPasswd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginPasswd() {
        return loginPasswd;
    }

    public void setLoginPasswd(String loginPasswd) {
        this.loginPasswd = loginPasswd;
    }
}
