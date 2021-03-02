package com.jqy.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.jqy.admin.model.po.User;
import com.jqy.result.CommonsReturn;
import com.jqy.result.ReturnCode;
import com.jqy.utils.JWT;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

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
        if(StringUtils.isEmpty(token)){//请求没有带认证信息
            //响应请求 返回json字符串信息
            response.getWriter().write(JSONObject.toJSONString(CommonsReturn.error(ReturnCode.LOGIN_DISABLED)));
            return false;
        }
        //解密Base64
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(token);
        //name+jqy+token
        String tokenInfo = new String(decode);
        //截取jqy  得到 name 和 token
        String[] jqies = tokenInfo.split("jqy");
        if (jqies.length != 2){
            //响应请求 返回json字符串信息
            response.getWriter().write(JSONObject.toJSONString(CommonsReturn.error(ReturnCode.LOGIN_DISABLED)));
            return false;
        }
        //认证token信息是否正确
        User unsign = JWT.unsign(jqies[1], User.class);
        if(unsign==null){//没验证过  需要拦截
            //响应请求 返回json字符串信息
            response.getWriter().write(JSONObject.toJSONString(CommonsReturn.error(ReturnCode.LOGIN_DISABLED)));
            return false;
        }
        return true;
    }
}
