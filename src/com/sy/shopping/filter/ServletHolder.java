package com.sy.shopping.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这个过滤器用于在每次请求之前将请求对象和响应对象放入ThreadLocal中
 * 使得在当前这个请求过程中的各个地方都能够用到本次的请求和响应对象
 */
public class ServletHolder implements Filter {
    private static ThreadLocal<HttpServletRequest> curReq = new ThreadLocal<>();
    private static ThreadLocal<HttpServletResponse> curRes = new ThreadLocal<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        //将本次的请求和响应对象放入ThreadLocal
        curReq.set(req);
        curRes.set(res);
        filterChain.doFilter(req, res);
        //每次请求完成后从ThreadLocal中移除本次的请求和响应对象
        curReq.remove();
        curRes.remove();
    }

    @Override
    public void destroy() {

    }

    public static HttpServletRequest getCurReq() {
        return curReq.get();
    }

    public static HttpServletResponse getCurRes() {
        return curRes.get();
    }
}
