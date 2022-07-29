package com.icbcintern.prepaycard.service;

import com.icbcintern.prepaycard.pojo.Merchant;

import java.util.List;

public interface MerchantService {
    // 注册商户
    boolean insertMerchant(Merchant merchant);

    // 按id查找
    Merchant getMerchantById(int id);

    //按 userName 查找
    Merchant getMerchantByName(String merchantName);

    //查询所有
    List<Merchant> queryMerchantInfo();

    //更新
    boolean updateMerchantById(Merchant merchant);

    //删除
    boolean deleteMerchantById(int id);

    // 增加 商户钱包关系表
    boolean insertMerchantWallet(String walletId, int merchantId);
}
