package com.sy.shopping.utils;

import com.alibaba.fastjson.JSON;
import com.sy.shopping.exception.BusinessException;
import com.sy.shopping.filter.ServletHolder;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Http操作相关的工具类
 */
public class HttpUtils {
    private HttpUtils() {

    }

    /**
     * 将请求参数对应的Map转换为对应的实体类
     *
     * @param clazz 要转换出来的目标数据类型
     * @param <T>   泛型参数
     * @return 转换后得到的Bean对象
     */
    public static <T> T convertMapToBean(Class<T> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object obj = clazz.newInstance();
        HttpServletRequest req = ServletHolder.getCurReq();
        BeanUtils.populate(obj, req.getParameterMap());
        return (T) obj;
    }

    /**
     * 获取指定名称的请求参数的值
     *
     * @param paramName 请求参数的名字
     * @param clazz     要转换出来的目标数据类型
     * @param errMsg    当转换发生错误的时候的提示信息
     * @return 请求参数的值
     */
    public static <T> T getParameter(String paramName, Class<T> clazz, String... errMsg) {
        HttpServletRequest req = ServletHolder.getCurReq();
        String paramValue = req.getParameter(paramName);
        if (StringUtils.isBlank(paramValue)) {
            return null;
        } else {
            try {
                //->int Integer.parseInt()
                if (int.class == clazz || Integer.class == clazz) {
                    return (T) new Integer(Integer.parseInt(paramValue));
                }
                //->dobule Double.parseDouble()
                if (double.class == clazz || Double.class == clazz) {
                    return (T) new Double(Double.parseDouble(paramValue));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                if (null != errMsg && errMsg.length != 0) {
                    throw new BusinessException(errMsg[0]);
                } else {
                    throw new BusinessException("参数格式不合法");
                }
            }
            if (String.class == clazz) {
                return (T) paramValue;
            }
        }
        return null;
    }


    /**
     * 向Session中存储属性
     *
     * @param name  属性名
     * @param value 属性值
     */
    public static void setSessionAttribute(String name, Object value) {
        HttpServletRequest req = ServletHolder.getCurReq();
        HttpSession session = req.getSession();
        session.setAttribute(name, value);
    }

    /**
     * 从Session中获取属性的值
     *
     * @param name session中的属性名
     * @param <T>  泛型参数
     * @return session中的属性值
     */
    public static <T> T getSessionAttribute(String name) {
        HttpSession session = ServletHolder.getCurReq().getSession();
        Object obj = session.getAttribute(name);
        if (null != obj) {
            return (T) obj;
        }
        return null;
    }

    /**
     * 在Cookie中设置属性
     *
     * @param name   Cookie的名字
     * @param value  Cookie的值
     * @param expire 过期时间
     */
    public static void setCookie(String name, String value, int expire) {
        HttpServletResponse res = ServletHolder.getCurRes();
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(expire);
        cookie.setPath("/");
        res.addCookie(cookie);
    }

    /**
     * 根据Cookie的名字获取Cookie的值
     *
     * @param name Cookie的名字
     * @return Cookie的值
     */
    public static String getCookie(String name) {
        Cookie[] cookies = ServletHolder.getCurReq().getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(name, cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 删除Cookie
     *
     * @param name 要删除的Cookie的名字
     */
    public static void delCookie(String name) {
        HttpServletResponse res = ServletHolder.getCurRes();
        Cookie[] cookies = ServletHolder.getCurReq().getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(name, cookie.getName())) {
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    res.addCookie(cookie);
                }
            }
        }
    }

    /**
     * 在响应中写出JSON格式的数据
     *
     * @param obj 要转换为JSON的数据类型
     */
    public static void writeJson(Object obj) throws IOException {
        HttpServletResponse res = ServletHolder.getCurRes();
        res.setContentType("application/json;charset=utf-8");
        PrintWriter pw = res.getWriter();
        pw.print(JSON.toJSONString(obj));
    }
}
