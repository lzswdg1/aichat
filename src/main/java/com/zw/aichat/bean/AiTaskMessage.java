package com.zw.aichat.bean;

import lombok.Data;

@Data
public class AiTaskMessage {


    //提问内容
    private String msg;

    private  Long userid;


    //标记id
    private  String taskId;

    private  Long modelId;

}
