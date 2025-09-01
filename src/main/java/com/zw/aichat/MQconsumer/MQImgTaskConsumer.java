package com.zw.aichat.MQconsumer;

import com.alibaba.fastjson.JSON;
import com.zw.aichat.bean.AiTaskMessage;
import com.zw.aichat.bean.DatabaseAi;
import com.zw.aichat.bean.QuestionBO;
import com.zw.aichat.config.AiClientNew;
import com.zw.aichat.service.DatabaseAiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RocketMQMessageListener(topic = "ai-task-topic-img",consumerGroup = "test-consumer-group1")
public class MQImgTaskConsumer implements RocketMQListener<String> {
    @Resource
    private DatabaseAiService databaseAiService;

    @Resource
    private AiClientNew aiClientNew;

    @Resource(name = "aiThreadPool")
    private ThreadPoolExecutor aiThreadPool;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(String message) {
        aiThreadPool.execute(()->{
            log.info("接受到询问主体: "+ message);
            AiTaskMessage aiTaskMessage = JSON.parseObject(message, AiTaskMessage.class);
            DatabaseAi databaseAi =new DatabaseAi();
            databaseAi.setUserId(aiTaskMessage.getModelId());  //使用父线程的loginId
            databaseAi.setId(aiTaskMessage.getModelId());
            List<DatabaseAi> databaseAis =databaseAiService.selectByDatabaseAi(databaseAi);

            QuestionBO questionBO = new QuestionBO();
            questionBO.setQuestion(aiTaskMessage.getMsg());
            questionBO.setAiApiKey(databaseAis.get(0).getAiApiKey());
            questionBO.setAiApiUrl(databaseAis.get(0).getAiApiUrl());
            questionBO.setAiApiModel(databaseAis.get(0).getAiApiModel());
            String answer = aiClientNew.askImg(questionBO).toString();


            //将图片路径存入redis中
            redisTemplate.opsForValue().set("ai:result:" + aiTaskMessage.getTaskId(),answer,10, TimeUnit.MINUTES);
        });
    }
}
