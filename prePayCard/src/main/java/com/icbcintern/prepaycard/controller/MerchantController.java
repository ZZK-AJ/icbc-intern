package com.icbcintern.prepaycard.controller;

import com.icbcintern.prepaycard.pojo.Merchant;
import com.icbcintern.prepaycard.service.MerchantService;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MerchantController {
    @Autowired
    MerchantService merchantService;

    /**
     * 商家 注册
     * todo 商家注册成功后，自动生成钱包id 商家及钱包的关系表，钱包账户表 的记录
     */
    @PostMapping("/merchantRegister")
    public Result MerchantRegister(@RequestBody Map<String, String> merchantMap) {
        Merchant merchant = new Merchant();
        Result result = new Result();

        if (merchantMap.get("merchantName").isEmpty() || merchantMap.get("loginPasswd").isEmpty()) {
            result.setMsg("商户名和密码不能为空");
            return result;
        }

        // 查询是否有重复的用户名
        Merchant existMerchant = merchantService.getMerchantByName(merchantMap.get("merchantName"));
        if (existMerchant != null) {
            result.setMsg("该商户名已存在");
            return result;
        }

        merchant.setMerchantName(merchantMap.get("merchantName"));
        merchant.setLoginPasswd(merchantMap.get("loginPasswd"));
        merchant.setMerchantInfo(merchantMap.get("merchantInfo"));
        boolean r = merchantService.insertMerchant(merchant);
        if (r) {
            result.setMsg("商户注册成功");

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
    public Result getAllUserInfo() {
        Result result = new Result();
        List<Merchant> userList = merchantService.queryMerchantInfo();
        if (userList != null) {
            result.setData(userList);
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
        // todo 根据对应商户的 id 赋值
        merchant.setId(Integer.valueOf(merchantMap.get("id")));
        merchant.setMerchantName(merchantMap.get("merchantName"));
        merchant.setLoginPasswd(merchantMap.get("loginPasswd"));
        merchant.setMerchantInfo(merchantMap.get("merchantInfo"));
        boolean r = merchantService.updateMerchantById(merchant);
        if (r) {
            result.setMsg("商户更新成功");
        } else {
            result.setCode(1);
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
