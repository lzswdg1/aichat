package com.zw.aichat.bean;

import lombok.Data;

@Data
public class QuestionBO {

    //问题内容
    private String question;

    private String aiApiKey;

    private String aiApiUrl;

    private String aiApiModel;
}
