package com.icbcintern.prepaycard.service.impl;

import com.icbcintern.prepaycard.mapper.UserMapper;
import com.icbcintern.prepaycard.pojo.User;
import com.icbcintern.prepaycard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    /**
     * 添加 user
     */
    @Transactional
    @Override
    public boolean insertUser(User user) {
        if (user.getName() != null && !"".equals(user.getName())) {
            try {
                System.out.println(user.getName());
                int effectNum = userMapper.insertUser(user);

                if (effectNum > 0) {
                    // 如果影响行数大于0，那么就是增加成功
                    System.out.println("增加成功，主键为：" + user.getId());
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
     * 按id查找 user
     */
    @Override
    public User getUserById(int id) {
        return userMapper.getUserInfoById(id);
    }


    /**
     * 按 userName 查找 user
     */
    @Override
    public User getUserByUserName(String userName) {
        return userMapper.getUserByUserName(userName);
    }

    /**
     * 查询所有 user
     */
    @Override
    public List<User> queryUserInfo() {
        return userMapper.queryUserInfo();
    }

    /**
     * 更新 user
     */
    @Transactional
    @Override
    public boolean updateUserById(User user) {
        if (user.getId() != null) {
            try {
                int effectNum = userMapper.updateUserById(user);
                if (effectNum > 0) {
                    //如果影响行数大于0，更新成功
                    System.out.println("更新成功，主键为：" + user.getId());
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
     * 删除 user
     */
    @Override
    public boolean deleteUserById(int id) {
        try {
            int effectNum = userMapper.deleteUserById(id);
            return effectNum > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertUserWallet(String walletID, int userId) {
        try {
            int effectNum = userMapper.insertUserWallet(walletID, userId);

            if (effectNum > 0) {
                // 如果影响行数大于0，那么就是增加成功
                return true;
            } else {
                throw new RuntimeException("插入信息失败,插入行数有误");
            }
        } catch (Exception e) {
            throw new RuntimeException("插入信息失败了:" + e.getMessage());
        }
    }
}
