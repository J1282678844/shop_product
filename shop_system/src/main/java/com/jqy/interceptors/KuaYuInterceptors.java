package com.jqy.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName KuaYuInterceptors
 * @Author 阳
 * @Date 2021/2/21 19:04
 * @Version 1.0
 **/

public class KuaYuInterceptors implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取跨域的路径
        String origin = request.getHeader("Origin");
        //设置允许跨域的路径
        response.addHeader("Access-Control-Allow-Origin", origin);
        //设置运行cookie
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return true;
    }
}
