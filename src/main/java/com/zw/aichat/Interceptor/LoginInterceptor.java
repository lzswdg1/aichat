package com.zw.aichat.Interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.zw.aichat.context.LoginContextHolder;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res,Object handler) throws Exception {
        if("OPTIONS".equalsIgnoreCase(req.getMethod())){
            res.setStatus(HttpServletResponse.SC_OK);
            return false;
        }
        if(StpUtil.isLogin()) {
            Long loginId = StpUtil.getLoginIdAsLong();
            LoginContextHolder.set("loginId", loginId);
        }
        return  true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res,Object handler, @Nullable Exception ex) throws Exception {
        LoginContextHolder.remove();
    }
}
