package com.sy.shopping.service.impl;

import com.sy.shopping.constant.BusinessConstant;
import com.sy.shopping.constant.RegexConstant;
import com.sy.shopping.dao.UserDao;
import com.sy.shopping.dao.impl.UserDaoImpl;
import com.sy.shopping.entity.User;
import com.sy.shopping.exception.BusinessException;
import com.sy.shopping.service.HistoryService;
import com.sy.shopping.service.UserService;
import com.sy.shopping.utils.CommonUtils;
import com.sy.shopping.utils.HttpUtils;
import com.sy.shopping.utils.RedisUtils;
import com.sy.shopping.vo.UserRegistVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();
    private HistoryService historyService = new HistoryServiceImpl();

    @Override
    public void regist(UserRegistVo userRegistVo, String correctValidateCode) {
        String userName = userRegistVo.getUserName();
        String pwd = userRegistVo.getPwd();
        String pwdConfirm = userRegistVo.getPwdConfirm();
        String phone = userRegistVo.getPhone();
        String validateCode = userRegistVo.getValidateCode();

        //1.用户名
        if (StringUtils.isBlank(userName)) {
            throw new BusinessException("用户名不能为空");
        }
        if (!CommonUtils.validateRegex(userName, RegexConstant.USER_NAME_REGEX)) {
            throw new BusinessException("用户名不合法");
        }
        try {
            if (null != userDao.getUserByUserName(userName)) {
                throw new BusinessException("用户名已存在");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //2.密码
        if (StringUtils.isBlank(pwd) || StringUtils.isBlank(pwdConfirm)) {
            throw new BusinessException("密码不能为空");
        }
        if (!CommonUtils.validateRegex(pwd, RegexConstant.STRONG_PWD_REGEX)) {
            throw new BusinessException("密码不是强密码");
        }
        if (!StringUtils.equals(pwd, pwdConfirm)) {
            throw new BusinessException("两次输入的密码不一致");
        }

        //3.手机号
        if (StringUtils.isBlank(phone)) {
            throw new BusinessException("手机号不能为空");
        }
        if (!CommonUtils.validateRegex(phone, RegexConstant.PHONE_NUMBER_REGEX)) {
            throw new BusinessException("手机号格式不合法");
        }
        try {
            if (null != userDao.getUserByPhone(phone)) {
                throw new BusinessException("手机号格已经被注册了");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //4.验证码
        if (StringUtils.isBlank(validateCode)) {
            throw new BusinessException("验证码不能为空");
        }
        if (!StringUtils.equalsIgnoreCase(validateCode, correctValidateCode)) {
            throw new BusinessException("请输入正确的验证码");
        }


        //校验通过后将用户信息写入数据库
        //密码MD5加密后存入数据库
        userRegistVo.setPwd(DigestUtils.md5Hex(pwd));
        try {
            if (!userDao.addUser(userRegistVo)) {
                throw new BusinessException("插入用户信息失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public User login(String userName, String pwd) {
        //1.用户名
        if (StringUtils.isBlank(userName)) {
            throw new BusinessException("用户名不能为空");
        }
        User user;
        try {
            user = userDao.getUserByUserName(userName);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if (null == user) {
            throw new BusinessException("用户不存在");
        }

        //2.密码
        if (StringUtils.isBlank(pwd)) {
            throw new BusinessException("密码不能为空");
        }
        if (!StringUtils.equals(user.getPwd(), DigestUtils.md5Hex(pwd))) {
            throw new BusinessException("密码不正确");
        }

        //如果登录成功就返回用户信息
        //为了安全起见，返回给前端的数据中密码置空
        user.setPwd(null);

        //1.合并历史记录的操作
        historyService.mergeHistory(user.getId());

        return user;
    }

    @Override
    public User getLoginUser() {
        //1.从Cookie中获取Token的值
        String token = HttpUtils.getCookie(BusinessConstant.COOKIE_TOKEN_NAME);
        //(1)如果Cookie中没有对应的Token
        if (StringUtils.isBlank(token)) {
            return null;
        }
        //(2)Cookie中存在Token
        else {
            String key = BusinessConstant.REDIS_USER_PREFIX + token;
            User user = RedisUtils.getObj(key);
            //(2-1)如果此时Redis中没有对应的Key
            if (null == user) {
                return null;
            }
            //(2-2)Redis中有对应的Key
            return user;
        }
    }
}
