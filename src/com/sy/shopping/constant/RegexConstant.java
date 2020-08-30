package com.sy.shopping.constant;

/**
 * 用于存放正则表达式相关的常量
 */
public class RegexConstant {
    /**
     * 用户名对应的正则表达式
     */
    public static final String USER_NAME_REGEX = "[a-zA-Z][a-zA-Z0-9_]{3,15}";
    /**
     * 强密码对应的正则表达式
     */
    public static final String STRONG_PWD_REGEX = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}";
    /**
     * 手机号对应的正则表达式
     */
    public static final String PHONE_NUMBER_REGEX = "(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}";
}
