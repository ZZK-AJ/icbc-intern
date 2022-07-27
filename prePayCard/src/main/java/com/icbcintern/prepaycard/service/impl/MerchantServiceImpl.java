package com.icbcintern.prepaycard.service.impl;

import com.icbcintern.prepaycard.mapper.MerchantMapper;
import com.icbcintern.prepaycard.pojo.Merchant;

import com.icbcintern.prepaycard.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    MerchantMapper merchantMapper;

    /**
     * 添加 merchant
     */
    @Transactional
    @Override
    public boolean insertMerchant(Merchant merchant) {
        if (merchant.getMerchantName() != null && !"".equals(merchant.getMerchantName())) {
            try {
                int effectNum = merchantMapper.insertMerchant(merchant);

                if (effectNum > 0) {
                    // 如果影响行数大于0，那么就是增加成功
                    System.out.println("增加成功，主键为：" + merchant.getId());
                    return true;
                } else {
                    throw new RuntimeException("插入信息失败,插入行数有误");
                }
            } catch (Exception e) {
                throw new RuntimeException("插入信息失败了:" + e.getMessage());
            }

        } else {
            throw new RuntimeException("输入信息不能为空！");
        }
    }


    /**
     * 按id查找 merchant
     */
    @Override
    public Merchant getMerchantById(int id) {
        return merchantMapper.getMerchantInfoById(id);
    }


    /**
     * 按 userName 查找 merchant
     */
    @Override
    public Merchant getMerchantByName(String merchantName) {
        return merchantMapper.getMerchantByName(merchantName);
    }

    /**
     * 查询所有 merchant
     */
    @Override
    public List<Merchant> queryMerchantInfo() {
        return merchantMapper.queryMerchantInfo();
    }

    /**
     * 更新 merchant
     */
    @Transactional
    @Override
    public boolean updateMerchantById(Merchant merchant) {
        if (merchant.getId() != null) {
            try {
                int effectNum = merchantMapper.updateMerchantById(merchant);
                if (effectNum > 0) {
                    //如果影响行数大于0，更新成功
                    System.out.println("更新成功，主键为：" + merchant.getId());
                    return true;
                } else {
                    throw new RuntimeException("更新信息失败,插入行数有误");
                }
            } catch (Exception e) {
                throw new RuntimeException("更新信息失败了:" + e.getMessage());
            }

        } else {
            throw new RuntimeException("信息不能为空！！！！");
        }

    }

    /**
     * 删除 merchant
     */
    @Override
    public boolean deleteMerchantById(int id) {
        try {
            int effectNum = merchantMapper.deleteMerchantById(id);
            return effectNum > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
