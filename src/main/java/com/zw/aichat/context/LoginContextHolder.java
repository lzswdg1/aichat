package com.zw.aichat.context;

import cn.dev33.satoken.context.SaTokenContext;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.context.model.SaTokenContextModelBox;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

//登入上下文对象
public class LoginContextHolder {
    private static final InheritableThreadLocal<Map<String,Object>> THREAD_LOCAL =
            new InheritableThreadLocal<Map<String,Object>>();

    public static final String LOGIN_ID_KEY = "loginId";
    public static Context withLoginId(Long loginId) { return
    Context.of(LOGIN_ID_KEY,loginId);
    }

    public static void set(String key,Object value) {
        Map<String,Object> map = getThreadLocalMap();
        map.put(key,value);
    }

    public static Long getLoginId() {
        return (Long) getThreadLocalMap().get("loginId");
    }
    public static void remove() { THREAD_LOCAL.remove(); }
    public static Object get(String key) {
        Map<String,Object> threadLocalMap =getThreadLocalMap();
        return threadLocalMap.get(key);
    }

    public static Map<String,Object> getThreadLocalMap() {
        Map<String,Object> threadLocalMap = THREAD_LOCAL.get();
        if(Objects.isNull(threadLocalMap)) {
            threadLocalMap = new ConcurrentHashMap<>();
            THREAD_LOCAL.set(threadLocalMap);
        }
        return threadLocalMap;
    }
}
