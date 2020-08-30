package com.sy.shopping.utils;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang.StringUtils;

import java.util.Random;
import java.util.UUID;

/**
 * 封装一些自己的通用工具方法
 */
public class CommonUtils {
    private CommonUtils() {

    }

    /**
     * 验证给定的字符串src是否符合regex对应的正则表达式规范
     *
     * @param src   要验证的字符串
     * @param regex 正则表达式
     * @return 验证以后的结果
     */
    public static boolean validateRegex(String src, String regex) {
        if (null != src) {
            return src.matches(regex);
        }
        return false;
    }


    /**
     * 用于生成长度为length的指定字符串
     *
     * @param length 字符串的长度
     * @return 随机字符串
     */
    public static String generateRandomString(int length) {
        String str = "3456789abcdefghjkmnpqrstuvwxy";
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            result.append(str.charAt(random.nextInt(str.length())));
        }
        return result.toString();
    }

    /**
     * 生成UUID的方法
     *
     * @return UUID
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
