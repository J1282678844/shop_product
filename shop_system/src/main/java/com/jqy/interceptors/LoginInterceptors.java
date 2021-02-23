package com.jqy.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.jqy.utils.JWT;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TestInterceptors
 * @Author 阳
 * @Date 2021/2/21 18:55
 * @Version 1.0
 **/

public class LoginInterceptors implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //需要认证过才能访问
        //获取token值
        String token = request.getParameter("token");
        if(token==null){//请求没有带认证信息
            Map rs=new HashMap();
            rs.put("code",400);
            rs.put("data"," que shao  token");
            //响应请求 返回json字符串信息
            response.getWriter().write(JSONObject.toJSONString(rs));
            return  false;
        }
        //认证token信息是否正确
        Map unsign = JWT.unsign(token, Map.class);
        if(unsign==null){//没验证过  需要拦截
            Map rs=new HashMap();
            rs.put("code",500);
            rs.put("data","token is error");
            //响应请求 返回json字符串信息
            response.getWriter().write(JSONObject.toJSONString(rs));
            return  false;
        }
        return true;
    }
}
