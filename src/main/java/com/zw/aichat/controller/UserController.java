package com.zw.aichat.controller;


import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;
import com.zw.aichat.bean.User;
import com.zw.aichat.context.LoginContextHolder;
import com.zw.aichat.service.UserService;
import com.zw.aichat.bean.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;
    // 添加一个 ObjectMapper 实例，用于测试
    private final ObjectMapper objectMapper = new ObjectMapper();
    //登入
    @PostMapping("doLogin")
    public Result doLogin(@RequestBody User user) {
        Preconditions.checkArgument(!StringUtils.isBlank(user.getUsername()),"用户名不能为空");
        Preconditions.checkArgument(!StringUtils.isBlank(user.getPassword()),"密码不能为空");

        Long id = userService.doLogin(user);
//        if(id!=null){
//            StpUtil.login(id);
//            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
////            LoginContextHolder.set("loginId",id);
//            System.out.println(LoginContextHolder.getLoginId());
//            return Result.ok(tokenInfo);
//        }
//        return Result.fail("登入失败");
        if(id!=null){
            StpUtil.login(id);
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

            // 【关键调试步骤】: 在返回前，手动序列化并打印
            try {
                // 使用 Jackson (Spring Boot 默认) 打印
                System.out.println("--- Jackson序列化结果 ---");
                System.out.println(objectMapper.writeValueAsString(Result.ok(tokenInfo)));

                // 使用 FastJSON 打印
                System.out.println("--- FastJSON序列化结果 ---");
                System.out.println(JSON.toJSONString(Result.ok(tokenInfo)));
                System.out.println("--------------------------");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return Result.ok(tokenInfo);
        }
        return Result.fail("登入失败");
    }


    //注册
    @PostMapping("doRegister")
    public Result doRegister(@RequestBody User user) {
        Preconditions.checkArgument(!StringUtils.isBlank(user.getUsername()),"用户名不能为空");
        Preconditions.checkArgument(!StringUtils.isBlank(user.getPassword()),"密码不能为空");
        user.setManager("0");
        userService.doRegister(user);
        return Result.ok("注册成功");
    }

    //测试

    @PostMapping("test")
    public String doo(){
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return  (String) tokenInfo.getLoginId();
    }
}
