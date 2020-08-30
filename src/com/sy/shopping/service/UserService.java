package com.sy.shopping.service;

import com.sy.shopping.entity.User;
import com.sy.shopping.vo.UserRegistVo;

public interface UserService {
    /**
     * 注册操作
     *
     * @param userRegistVo        前端传过来的参数对应的类型
     * @param correctValidateCode 从Session中获取的正确的验证码的值
     */
    void regist(UserRegistVo userRegistVo, String correctValidateCode);

    /**
     * 登录操作
     *
     * @param userName 用户名密码
     * @param pwd      密码
     * @return 登录成功的用户信息
     */
    User login(String userName, String pwd);


    /**
     * 获取登录的用户信息
     *
     * @return 登录的用户信息
     */
    User getLoginUser();
}
