package com.sy.shopping.dao;

import com.sy.shopping.entity.User;
import com.sy.shopping.vo.UserRegistVo;

import java.sql.SQLException;

public interface UserDao {
    /**
     * 添加用户信息到数据库
     *
     * @param userRegistVo 注册时要向数据库写入的用户信息
     * @return 插入是否成功
     */
    boolean addUser(UserRegistVo userRegistVo) throws SQLException;


    /**
     * 根据用户名查找用户对象
     *
     * @param userName 用户名
     * @return 得到的用户对象
     */
    User getUserByUserName(String userName) throws SQLException;

    /**
     * 根据用手机号查找用户对象
     *
     * @param phone 手机号
     * @return 得到的用户对象
     */
    User getUserByPhone(String phone) throws SQLException;
}
