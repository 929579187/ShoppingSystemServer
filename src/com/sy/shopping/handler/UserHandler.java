package com.sy.shopping.handler;

import com.sy.shopping.annotations.RequestMapping;
import com.sy.shopping.constant.AppConstant;
import com.sy.shopping.constant.BusinessConstant;
import com.sy.shopping.entity.User;
import com.sy.shopping.exception.BusinessException;
import com.sy.shopping.service.HistoryService;
import com.sy.shopping.service.UserService;
import com.sy.shopping.service.impl.HistoryServiceImpl;
import com.sy.shopping.service.impl.UserServiceImpl;
import com.sy.shopping.utils.CommonUtils;
import com.sy.shopping.utils.HttpUtils;
import com.sy.shopping.utils.RedisUtils;
import com.sy.shopping.utils.SMSUtils;
import com.sy.shopping.vo.UserRegistVo;
import org.apache.commons.lang.StringUtils;

@RequestMapping("user")
public class UserHandler {
    private UserService userService = new UserServiceImpl();

    /**
     * 注册请求的具体处理
     */
    @RequestMapping("regist")
    public void registUser() throws Exception {
        //1.前端输入的参数
        UserRegistVo userRegistVo = HttpUtils.convertMapToBean(UserRegistVo.class);
        //2.Session中存储的验证码
        String correctValidateCode = HttpUtils.getSessionAttribute(BusinessConstant.SESSION_VALIDATE_CODE);

        userService.regist(userRegistVo, correctValidateCode);
    }

    /**
     * 发送验证码短信
     */
    @RequestMapping("sendValidateCode")
    public void sendValidateCode() {
        //要发送短信的手机号
        String phone = HttpUtils.getParameter("phone", String.class);
        //你输入的验证码和实际的验证码要进行对比--怎样去保留发送的验证码供提交注册的表单的时候去验证？
        //1.生成验证码
        String validateCode = CommonUtils.generateRandomString(BusinessConstant.VALIDATE_CODE_LENGTH);
        //2.将验证码存入Session中
        HttpUtils.setSessionAttribute(BusinessConstant.SESSION_VALIDATE_CODE, validateCode);
        SMSUtils.sendValidateCode(phone, validateCode);
    }

    /**
     * 登录操作
     *
     * @return 当前登录的用户的基本信息
     */
    @RequestMapping("login")
    public User login() {
        String userName = HttpUtils.getParameter("name", String.class);
        String pwd = HttpUtils.getParameter("pwd", String.class);
        //TODO:要将用户信息保存在会话中
        // 1.以前：Session
        // 2.今天：Redis
        User user = userService.login(userName, pwd);
        //1.在登录成功以后，在Cookie中存储一个Token，这个Token相当于是客户端的唯一凭证
        //类似SessionId的作用，用于区分唯一的客户端
        //(1)生成一个Token
        String token = CommonUtils.generateUuid();
        //(2)将Token写入Cookie
        HttpUtils.setCookie(BusinessConstant.COOKIE_TOKEN_NAME, token, BusinessConstant.LOGIN_EXPIRE_SECONDS);

        //2.在Redis中存储与当前客户端对应的登录用户的信息
        boolean success = RedisUtils.set(BusinessConstant.REDIS_USER_PREFIX + token, user);

        return user;
    }


    /**
     * 获取当前登录的用户的信息
     *
     * @return 登录用户的信息
     */
    @RequestMapping("loginUser")
    public User getLoginUser() {
        //1.从Cookie中获取Token的值
        String token = HttpUtils.getCookie(BusinessConstant.COOKIE_TOKEN_NAME);
        //(1)如果Cookie中没有对应的Token
        if (StringUtils.isBlank(token)) {
            throw new BusinessException("用户未登录");
        }
        //(2)Cookie中存在Token
        else {
            String key = BusinessConstant.REDIS_USER_PREFIX + token;
            User user = RedisUtils.getObj(key);
            //(2-1)如果此时Redis中没有对应的Key
            if (null == user) {
                throw new BusinessException("用户未登录");
            }
            //(2-2)Redis中有对应的Key
            return user;
        }
    }

    /**
     * 注销操作
     */
    @RequestMapping("logout")
    public void logout() {
        //1.先获取Cookie中的Token
        String token = HttpUtils.getCookie(BusinessConstant.COOKIE_TOKEN_NAME);
        //2.删除Cookie
        HttpUtils.delCookie(BusinessConstant.COOKIE_TOKEN_NAME);
        //3.删除Redis中对应的key
        RedisUtils.del(BusinessConstant.REDIS_USER_PREFIX + token);
    }
}
