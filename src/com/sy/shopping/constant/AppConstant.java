package com.sy.shopping.constant;

/**
 * 系统常量
 */
public class AppConstant {
    /**
     * 用于存放Handler类的包的名字
     */
    public static final String HANDLERS_PACKAGE = "com.sy.shopping.handler";

    /**
     * 上下文对象中用于存储映射信息的属性对应的名字
     */
    public static final String SERVLET_CONTEXT_HANDLER_MAPPING_LIST_ATTRIBUTE_NAME = "HANDLER_MAPPING_LIST";

    /**
     * 接口路径的固定前缀
     */
    public static final String SERVER_APIS_PREFIX = "/apis";

    public static final String SERVER_APIS_PATTERN = SERVER_APIS_PREFIX + "/*";

    /**
     * REDIS中返回的OK结果
     */
    public static final String REDIS_OK_RESP = "OK";
}
