package com.zw.aichat.Interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.zw.aichat.annotation.RateLimit;
import com.zw.aichat.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(rateLimit)")
    public Object around (ProceedingJoinPoint pjp, RateLimit rateLimit) throws  Throwable {
        //获取方法签名
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        //生成限流key(支持用户id)
        String key = "rate_limit:" + method.getDeclaringClass().getName() + ":" + method.getName();
        if(rateLimit.perUser()){
            key += ":user:" + StpUtil.getLoginIdAsString();
        }
        //加上当前窗口时间标识（粗粒度控制)
        Long window  = System.currentTimeMillis() / rateLimit.unit().toMillis(rateLimit.duration());
        key +=":win:" + window;

        //获取计数器
        RAtomicLong counter = redissonClient.getAtomicLong(key);
        if(!counter.isExists()) {
            counter.expire(rateLimit.duration(),rateLimit.unit());
        }
        //自增并判断是否超限
        Long count = counter.incrementAndGet();
        if(count> rateLimit.limit()) {
            throw  new GlobalExceptionHandler();
        }
        return pjp.proceed();
    }
}
