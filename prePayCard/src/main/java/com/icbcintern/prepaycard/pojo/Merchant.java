package com.icbcintern.prepaycard.pojo;


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
