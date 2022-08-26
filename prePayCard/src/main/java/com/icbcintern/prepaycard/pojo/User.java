package com.icbcintern.prepaycard.pojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Override
    public String toString() {
        ObjectMapper json = new ObjectMapper();
        try {
            return json.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return String.format("User(%s,%s,%s,%s)",id,name,loginPasswd,payPasswd);
        }
    }
}
