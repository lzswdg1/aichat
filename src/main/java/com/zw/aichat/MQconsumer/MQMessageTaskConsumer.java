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

@Component
@Slf4j
@RocketMQMessageListener(topic="ai-task-topic-message",consumerGroup = "test-consumer-group2")
public class MQMessageTaskConsumer implements RocketMQListener<String> {
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
            log.info("接收到询问主体: " + message);
            AiTaskMessage aiTaskMessage = JSON.parseObject(message, AiTaskMessage.class);
            DatabaseAi  databaseAi = new DatabaseAi();
            databaseAi.setUserId(aiTaskMessage.getUserid());
            databaseAi.setId(aiTaskMessage.getModelId());
            List<DatabaseAi> databaseAiList =databaseAiService.selectByDatabaseAi(databaseAi);

            QuestionBO questionBO = new QuestionBO();
            questionBO.setQuestion(aiTaskMessage.getMsg());
            questionBO.setAiApiKey(databaseAiList.get(0).getAiApiKey());
            questionBO.setAiApiUrl(databaseAiList.get(0).getAiApiUrl());
            questionBO.setAiApiModel(databaseAiList.get(0).getAiApiModel());

            String answer = aiClientNew.sendMessage(questionBO);
            log.info("Ai 返回的答案是: {}", answer);

            //将答案存入redis中
            redisTemplate.opsForValue().set("ai:result:" + aiTaskMessage.getTaskId(),answer,10, TimeUnit.MINUTES);
        });
    }
}
