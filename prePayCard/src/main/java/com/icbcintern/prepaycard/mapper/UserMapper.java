package com.icbcintern.prepaycard.mapper;

import com.icbcintern.prepaycard.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 添加user信息
     */
    @Insert("insert into user(id,user_name,login_passwd,pay_passwd) values (#{id},#{userName},#{loginPasswd},#{payPasswd})")
    int insertUser(User user);

    /**
     * 根据 ID 查询 user 信息
     */
    @Select("select * from user where id=#{id}")
    User getUserInfoById(int id);

    /**
     * 根据 用户名 查询 user 信息
     */
    @Select("select * from user where user_name=#{user_name}")
    User getUserByUserName(String userName);

    /**
     * 查询所有用户信息
     */
    @Select("select * from user")
    List<User> queryUserInfo();

    /**
     * 更新用户信息
     */
    @Update("update user set id=#{id},user_name=#{userName},login_passwd=#{loginPasswd},pay_passwd=#{payPasswd} where id=#{id}")
    int updateUserById(User user);

    /**
     * 删除用户信息
     */
    @Delete("delete from user where id=#{id}")
    int deleteUserById(int id);
}
