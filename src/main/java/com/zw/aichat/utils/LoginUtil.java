package com.zw.aichat.utils;

import com.zw.aichat.context.LoginContextHolder;

public class LoginUtil {

    public static Long getLoginId() {
        return LoginContextHolder.getLoginId();
    }
}
