package com.icbcintern.prepaycard.controller;

import com.icbcintern.prepaycard.pojo.Merchant;
import com.icbcintern.prepaycard.pojo.User;
import com.icbcintern.prepaycard.pojo.Wallet;
import com.icbcintern.prepaycard.service.MerchantService;
import com.icbcintern.prepaycard.service.WalletService;
import com.icbcintern.prepaycard.utils.JwtTools;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class MerchantController {
    @Autowired
    MerchantService merchantService;
    @Autowired
    WalletService walletService;

    /**
     * 商家 注册
     * todo 商家注册成功后，自动生成钱包id 商家及钱包的关系表，钱包账户表 的记录
     */
    @PostMapping("/merchantRegister")
    public Result MerchantRegister(@RequestBody Map<String, String> merchantMap) {
        Merchant merchant = new Merchant();
        Result result = new Result();

        if (merchantMap.get("merchantName").isEmpty() || merchantMap.get("loginPasswd").isEmpty()) {
            result.setMsg("注册失败，商户名和密码不能为空");
            return result;
        }
        Merchant existMerchant = merchantService.getMerchantByName(merchantMap.get("merchantName"));
        if (existMerchant != null) {
            result.setMsg("注册失败，该商户名已存在");
            return result;
        }

        merchant.setName(merchantMap.get("merchantName"));
        merchant.setLoginPasswd(merchantMap.get("loginPasswd"));
        merchant.setMerchantInfo(merchantMap.get("merchantInfo"));
        boolean r = merchantService.insertMerchant(merchant);
        if (r) {
            Wallet wallet = new Wallet();
            wallet.setWalletId(UUID.randomUUID().toString());
            wallet.setType(2);   // 钱包类型，用户 1, 商户 2, 运营方 3, 冻结 0
            long balance = 1000000 + (long) (Math.random() * (5000000 - 1000000 + 1));  // 余额默认范围 10000-50000
            wallet.setBalance(balance);
            Boolean res = walletService.insertWallet(wallet);
            if (res) {
                Merchant registeredMerchant = merchantService.getMerchantByName(merchant.getName());
                boolean b = merchantService.insertMerchantWallet(wallet.getWalletId(), registeredMerchant.getId());
                if (b) {
                    result.setMsg("商户注册成功，添加钱包及关系表成功");
                    return result;
                } else {
                    result.setCode(1);
                    result.setMsg("商户注册失败,未成功添加商户钱包关系表");
                }
            } else {
                result.setCode(1);
                result.setMsg("商户注册失败,未成功添加钱包");
            }
        } else {
            result.setCode(1);
            result.setMsg("商户注册失败");
        }
        return result;
    }

    /**
     * 按 id 查找 merchant
     */
    @GetMapping("/userInfoById/{id}")
    public Result getUserInfoById(@PathVariable("id") int id) {
        Result result = new Result();
        Merchant merchant = merchantService.getMerchantById(id);
        if (merchant != null) {
            result.setData(merchant);
        } else {
            result.setCode(1);
            result.setMsg("用户查询失败");
        }
        return result;
    }

    /**
     * 查询所有 merchant
     */
    @GetMapping("/merchantInfo")
    public Result getAllMerchantInfo() {
        Result result = new Result();
        List<Merchant> merchantList = merchantService.queryMerchantInfo();
        if (merchantList != null) {
            result.setData(merchantList);
        } else {
            result.setCode(1);
            result.setMsg("用户查询失败");
        }
        return result;
    }

    /**
     * 更新 merchant
     */
    @PostMapping("/updateMerchantById")
    public Result updateUserById(@RequestBody Map<String, String> merchantMap) {
        Result result = new Result();
        Merchant merchant = new Merchant();
        merchant.setId(Integer.valueOf(merchantMap.get("id")));
        merchant.setName(merchantMap.get("merchantName"));
        merchant.setLoginPasswd(merchantMap.get("loginPasswd"));
        merchant.setMerchantInfo(merchantMap.get("merchantInfo"));
        boolean r = merchantService.updateMerchantById(merchant);
        if (r) {
            Result.ok();
            result.setMsg("商户更新成功");
        } else {
            Result.unOk();
            result.setMsg("商户更新失败");
        }
        return result;
    }

    /**
     * 删除
     */
    @DeleteMapping("/deleteMerchantById/{id}")
    public Result deleteUserById(@PathVariable("id") int id) {
        Result result = new Result();
        boolean r = merchantService.deleteMerchantById(id);
        if (r) {
            result.setMsg("删除成功");
        } else {
            result.setMsg("删除失败");
            result.setCode(1);
        }
        return result;
    }

    /**
     * 用户登录，userName and id 唯一
     */
    @PostMapping("/merchantLogin")
    public Result userLogin(@RequestBody Map<String, String> merchantMap) {
        Result result = new Result();
        Merchant existMerchant = merchantService.getMerchantByName(merchantMap.get("merchantName"));
        if (existMerchant == null) {
            result.setMsg("该商户未注册,请先注册");
            result.setCode(1);
            return result;
        }
        if (existMerchant.getLoginPasswd().equals(merchantMap.get("loginPasswd"))) {
            result.setMsg("商户登录中");
            // 返回 token
            String token = JwtTools.createToken(existMerchant);
            result.setData(token);  // 返回 token
        } else {
            result.setMsg("商户登录失败，查询登录密码是否错误");
            result.setCode(1);
        }
        return result;
    }

    /**
     * 用户修改密码
     */
    @PostMapping("/changeMerchantPasswd")
    public Result changePasswd(@RequestBody Map<String, String> merchantMap) {
        Result result = new Result();
//        todo 修改密码
        return result;
    }

}
