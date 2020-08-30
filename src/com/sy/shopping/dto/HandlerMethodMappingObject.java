package com.sy.shopping.dto;

import java.lang.reflect.Method;

/**
 * 这个类描述类Handler中每个方法的访问路径、当前Handler的实例以及该访问路径对应的方法对象
 */
public class HandlerMethodMappingObject {
    /**
     * Handler中方法的访问路径：  Handler类注解的路径+Handler方法的路径
     */
    private String url;
    /**
     * 访问当前这个方法需要用到的Handler实例对象
     */
    private Object handlerObj;
    /**
     * 访问这个URL的时候需要调用的方法对象
     */
    private Method method;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getHandlerObj() {
        return handlerObj;
    }

    public void setHandlerObj(Object handlerObj) {
        this.handlerObj = handlerObj;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "HandlerMethodMappingObject{" +
                "url='" + url + '\'' +
                ", handlerObj=" + handlerObj +
                ", method=" + method +
                '}';
    }
}
