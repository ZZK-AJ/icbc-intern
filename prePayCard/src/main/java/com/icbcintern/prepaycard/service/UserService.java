package com.icbcintern.prepaycard.service;

import com.icbcintern.prepaycard.pojo.User;

import java.util.List;

public interface UserService {
    // 注册用户
    boolean insertUser(User user);

    //按id查找
    User getUserById(int id);

    //按 userName 查找
    User getUserByUserName(String userName);

    //查询所有
    List<User> queryUserInfo();

    //更新
    boolean updateUserById(User user);

    //删除
    boolean deleteUserById(int id);

    // 增加 用户钱包关系表
    boolean insertUserWallet(String walletID, int userID);
}
