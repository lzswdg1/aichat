package com.zw.aichat.bean;

import lombok.Data;

@Data
public class User {
    //主键
    private   Long id;
    private   String  username;
    private   String  manager;
    private   String  password;
}
