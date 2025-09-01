package com.zw.aichat.Interceptor;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.Resource;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //定义自己的拦截器：记录loginId到ThreadLocal中
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login","/user/doRegister")
                .order(1);
    }

//    @Bean
//    public SaServletFilter getSaServletFilter() {
//        return new SaServletFilter()
//                .addExclude("/ai/ask","/user/doLogin","/user/doRegister")
//                .addInclude("/**")
//                .setAuth(obj -> StpUtil.checkLogin());
//    }
@Bean
public SaServletFilter getSaServletFilter() {
    return new SaServletFilter()
            // --- 定义需要拦截的路由 ---
            .addInclude("/**") // 拦截所有路由

            // --- 定义需要放行的（公开）路由 ---
            .addExclude("/favicon.ico")
            .addExclude("/user/doLogin")     // <-- 放行登录接口
            .addExclude("/user/doRegister")  // <-- 放行注册接口

            // --- 认证函数 ---
            // 每个被拦截的请求都会执行这里的代码
            .setAuth(obj -> {
                // 检查是否登录
                StpUtil.checkLogin();
            })

            // --- 异常处理函数 ---
            // 认证失败时执行
            .setError(e -> {
                SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
                // 返回统一的 JSON 错误信息
                return "{\"code\": 401, \"msg\": \"认证失败：" + e.getMessage() + "\"}";
            });
}
}
