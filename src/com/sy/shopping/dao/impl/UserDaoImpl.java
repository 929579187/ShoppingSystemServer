package com.sy.shopping.dao.impl;

import com.sy.shopping.dao.UserDao;
import com.sy.shopping.entity.User;
import com.sy.shopping.utils.DBUtils;
import com.sy.shopping.vo.UserRegistVo;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    private QueryRunner runner = new QueryRunner();

    @Override
    public boolean addUser(UserRegistVo userRegistVo) throws SQLException {
        try (Connection con = DBUtils.getConnection()) {
            return runner.update(con, "insert into user(username,pwd,phone) values(?,?,?)",
                    userRegistVo.getUserName(), userRegistVo.getPwd(), userRegistVo.getPhone()) > 0;
        }
    }


    @Override
    public User getUserByUserName(String userName) throws SQLException {
        try (Connection con = DBUtils.getConnection()) {
            return runner.query(con, "select * from user where username=?",
                    new BeanHandler<>(User.class),
                    userName);
        }
    }

    @Override
    public User getUserByPhone(String phone) throws SQLException {
        try (Connection con = DBUtils.getConnection()) {
            return runner.query(con, "select * from user where phone=?",
                    new BeanHandler<>(User.class),
                    phone);
        }
    }
}
