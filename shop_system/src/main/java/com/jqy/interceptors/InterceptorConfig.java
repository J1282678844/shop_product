package com.jqy.interceptors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName InterceptorConfig
 * @Author 阳
 * @Date 2021/2/21 18:58
 * @Version 1.0
 **/
@Configuration //声明是 配置类
public class InterceptorConfig implements WebMvcConfigurer {

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //注册 跨域 的拦截器
        InterceptorRegistration kuayu = registry.addInterceptor(new KuaYuInterceptors());
        //设置拦截的路径
        kuayu.addPathPatterns("/**");

        //注册第一个拦截器
        InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptors());
        //设置拦截的路径
        registration.addPathPatterns("/**");
        //设置不拦截的路径
        registration.excludePathPatterns("/admin/user/login");
    }
}
