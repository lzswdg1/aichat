package com.zw.aichat;

import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan("com.zw.aichat.dao")
@Import(RocketMQAutoConfiguration.class)
public class AichatApplication {

    public static void main(String[] args) {
        SpringApplication.run(AichatApplication.class, args);
    }

}
