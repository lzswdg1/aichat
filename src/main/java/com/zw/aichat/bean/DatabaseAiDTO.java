package com.zw.aichat.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class DatabaseAiDTO {
    //主键
    private Long id;

    private String  aiApiModel;
}
