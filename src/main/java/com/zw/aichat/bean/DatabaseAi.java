package com.zw.aichat.bean;

import lombok.Data;

@Data
public class DatabaseAi {

    //主键
    private Long id;

    private String aiApiKey;

    private String aiApiUrl;

    private String aiApiModel;

    private Long userId;
}
