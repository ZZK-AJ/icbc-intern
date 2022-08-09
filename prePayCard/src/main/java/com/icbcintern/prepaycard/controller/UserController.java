package com.icbcintern.prepaycard.controller;

import com.icbcintern.prepaycard.pojo.User;
import com.icbcintern.prepaycard.pojo.Wallet;
import com.icbcintern.prepaycard.utils.JwtTools;
import com.icbcintern.prepaycard.service.UserService;
import com.icbcintern.prepaycard.service.WalletService;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    WalletService walletService;

    /**
     * user 注册
     */
    @PostMapping("/UserRegister")
    public Result UserRegister(@RequestBody Map<String, String> userMap) {
        User user = new User();
        Result result = new Result();

        if (userMap.get("userName").isEmpty() || userMap.get("loginPasswd").isEmpty() || userMap.get("payPasswd").isEmpty()) {
            result.setMsg("用户名和密码不能为空");
            return result;
        }

        // 查询是否有重复的用户名
        User existUser = userService.getUserByUserName(userMap.get("userName"));
        if (existUser != null) {
            result.setMsg("该用户名已存在");
            return result;
        }

        user.setName(userMap.get("userName"));
        user.setLoginPasswd(userMap.get("loginPasswd"));
        user.setPayPasswd(userMap.get("payPasswd"));
        boolean r= userService.insertUser(user);
        // 增加 wallet 与 userWallet 表记录
        if (r) {
            Wallet wallet = new Wallet();
            wallet.setWalletId(UUID.randomUUID().toString());
            wallet.setType(1);   // 钱包类型，用户 1, 商户 2, 运营方 3, 冻结 0
            long balance = 100000 + (long) (Math.random() * (500000 - 100000 + 1));  // 余额默认范围 1000-5000
            wallet.setBalance(balance);
            Boolean res = walletService.insertWallet(wallet);
            if (res) {
                User registeredUser = userService.getUserByUserName(user.getName());
                boolean b = userService.insertUserWallet(wallet.getWalletId(), registeredUser.getId());
                if (b) {
                    result.setMsg("用户注册成功，添加钱包及关系表成功");
                    return result;
                } else {
                    result.setCode(1);
                    result.setMsg("用户注册失败,未成功添加用户钱包关系表");
                }
            } else {
                result.setCode(1);
                result.setMsg("用户注册失败,未成功添加钱包");
            }
        }else {
            result.setCode(1);
            result.setMsg("用户注册失败");
        }
        return result;
    }

    /**
     * 按 id 查找 user
     */
    @GetMapping("/UserInfoById/{id}")
    public Result getUserInfoById(@PathVariable("id") int id){
        Result result = new Result();
        User user = userService.getUserById(id);
        if (user != null) {
            result.setData(user);
        } else {
            result.setCode(1);
            result.setMsg("用户查询失败");
        }
        return result;
    }

    /**
     * 查询所有 user
     */
    @GetMapping("/UserInfo")
    public Result getAllUserInfo(){
        Result result = new Result();
        List<User> userList = userService.queryUserInfo();
        if (userList != null) {
            result.setData(userList);
        } else {
            result.setCode(1);
            result.setMsg("用户查询失败");
        }
        return result;
    }

    /**
     * 更新 user
     */
    @PostMapping("/updateUserById")
    public Result updateUserById(@RequestBody Map<String,String> userMap){
        Result result = new Result();
        User user = new User();
        // todo 根据对应用户的 id 赋值
        user.setId(Integer.valueOf(userMap.get("id")));
        user.setName(userMap.get("userName"));
        user.setLoginPasswd(userMap.get("loginPasswd"));
        user.setPayPasswd(userMap.get("payPasswd"));
        boolean r= userService.updateUserById(user);
        if (r) {
            Result.ok();
            result.setMsg("用户更新成功");
        } else {
            Result.unOk();
            result.setMsg("用户更新失败");
        }
        return result;
    }

    /**
     * 删除
     */
    @DeleteMapping("/deleteUserById/{id}")
    public Result deleteUserById(@PathVariable("id") int id){
        Result result = new Result();
        boolean r= userService.deleteUserById(id);
        if (r){
            result.setMsg("删除成功");
        }else {
            result.setMsg("删除失败");
            result.setCode(1);
        }
        return result;
    }

    /**
     * 用户登录，userName and id 唯一
     */
    @PostMapping("/UserLogin")
    public Result userLogin(@RequestBody Map<String, String> userMap) {
        Result result = new Result();
        User existUser = userService.getUserByUserName(userMap.get("userName"));
        if (existUser == null) {
            result.setMsg("该用户未注册,请先注册");
            result.setCode(1);
            return result;
        }
        if (existUser.getLoginPasswd().equals(userMap.get("loginPasswd"))) {
            result.setMsg("用户登录中");
//            String token = TokenUtil.genToken(existUser);
            String token = JwtTools.createToken(existUser);
            result.setData(token);  // 返回 token

        } else {
            result.setMsg("用户登录失败，查询登录密码是否错误");
            result.setCode(1);
        }
        return result;
    }

    /**
     * 用户修改密码
     */
    @PostMapping("/changeUserPasswd")
    public Result changePasswd(@RequestBody Map<String, String> userMap) {
        Result result = new Result();
//        todo 修改密码
        return result;
    }


}

