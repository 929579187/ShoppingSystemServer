package com.sy.shopping.filter;

import com.sy.shopping.constant.AppConstant;
import com.sy.shopping.dto.Result;
import com.sy.shopping.entity.User;
import com.sy.shopping.service.UserService;
import com.sy.shopping.service.impl.UserServiceImpl;
import com.sy.shopping.utils.HttpUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SessionFilter implements Filter {
    private List<String> sessionInteceptorUriList;
    private UserService userService = new UserServiceImpl();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        //获取实际请求的URI
        String uri = req.getRequestURI()
                .replace(req.getContextPath(), "")
                .replace(AppConstant.SERVER_APIS_PREFIX,"");
        //判断请求的URI是否在要拦截的路径列表中
        //1.不在列表中直接放行
        if (!sessionInteceptorUriList.contains(uri)) {
            chain.doFilter(req, res);
        }
        //2.在列表中判断Session中有无用户信息
        else {
            User user = userService.getLoginUser();
            //(2-1)已经登录
            if (null != user) {
                chain.doFilter(req, res);
            }
            //(2-2)未登录
            else {
                HttpUtils.writeJson(Result.buildFailure("用户尚未登录"));
            }
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //在过滤器初始化的时候读取所有需要拦截的URI列表
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("session-inteceptor-uris")) {
            sessionInteceptorUriList = IOUtils.readLines(is);
        } catch (Exception e) {
            System.out.println("加载URI列表失败...");
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }
}
