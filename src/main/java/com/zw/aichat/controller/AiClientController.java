package com.zw.aichat.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.zw.aichat.bean.*;
import com.zw.aichat.config.AiClient;
import com.zw.aichat.config.AiClientNew;
import com.zw.aichat.context.LoginContextHolder;
import com.zw.aichat.service.DatabaseAiService;
import com.zw.aichat.service.UserService;
import com.zw.aichat.utils.LoginUtil;
import com.zw.aichat.bean.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;


import java.util.List;
import java.util.UUID;
import com.zw.aichat.annotation.RateLimit;
import static net.sf.jsqlparser.parser.feature.Feature.limit;

@RestController
@RequestMapping("/ai")
@Slf4j
public class AiClientController {
    @Resource
    private AiClient aiClient;

    @Resource
    private DatabaseAiService databaseAiService;

    @Resource
    private UserService userService;

    @Resource
    private AiClientNew aiClientNew;

    @Resource
    private RedissonClient redissonClient;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/{taskId}")
    public Result<String> getAiAnswer(@PathVariable String taskId, @RequestHeader("satoken") String token) {
        log.info("taskId:{},token:{}", taskId, token);
        String result =(String) redisTemplate.opsForValue().get("ai:result:" + taskId);
        if(StringUtils.isEmpty(result)){
            return Result.ok(null);  //尚未完成
        }
        return  Result.ok(result);
    }


    //Ai对话
    @RateLimit(limit= 5, duration = 1)
    @PostMapping(value="/ask")
    public  Result<String> ask(@RequestBody QuestionDTO questionDTO){
        AiTaskMessage aiTaskMessage = new AiTaskMessage();
        aiTaskMessage.setMsg(questionDTO.getQuestion());
        String taskId = UUID.randomUUID().toString();
        aiTaskMessage.setTaskId(taskId);
        aiTaskMessage.setUserid(Long.valueOf(StpUtil.getLoginIdAsString()));
        aiTaskMessage.setModelId(questionDTO.getId());
        log.info(aiTaskMessage.toString());
        rocketMQTemplate.convertAndSend("ai-task-topic-message", JSON.toJSONString(aiTaskMessage));
        return Result.ok(taskId);
    }
    //ai对话生成图片
    @RateLimit(limit= 5, duration= 1)
    @PostMapping(value="/askImg")
    public Result<String> askImg(@RequestBody QuestionDTO questionDTO) {
        AiTaskMessage aiTaskMessage = new AiTaskMessage();
        aiTaskMessage.setMsg(questionDTO.getQuestion());
        String taskId = UUID.randomUUID().toString();
        aiTaskMessage.setTaskId(taskId);
        aiTaskMessage.setUserid(Long.valueOf(StpUtil.getLoginIdAsString()));
        log.info(aiTaskMessage.toString());
        aiTaskMessage.setModelId(questionDTO.getId());
        rocketMQTemplate.convertAndSend("ai-task-topic-img",JSON.toJSONString(aiTaskMessage));
        return Result.ok(taskId);
    }

    //查询用户拥有的模型
    @GetMapping("/selectModelByUserId")
    public Result<List<Long>> selectByUserId(@RequestHeader("satoken") String token) {
        System.out.println("前端传递过来的token是:" + token);
        DatabaseAi databaseAi = new DatabaseAi();
        databaseAi.setUserId(LoginUtil.getLoginId());
        List<DatabaseAi> databaseAis = databaseAiService.selectByDatabaseAi(databaseAi);
        List<DatabaseAiDTO> modelIds = databaseAis.stream().map(databaseAiInfo ->{
            Long id = databaseAiInfo.getId();
            String aiApiModel =databaseAiInfo.getAiApiModel();
            return new DatabaseAiDTO(id, aiApiModel);
        }).toList();
        return Result.ok(modelIds);
    }

    //插入模型
    @PostMapping(value = "/addModel")
    public Result addModel(@RequestBody DatabaseAi databaseAi){
        Preconditions.checkArgument(!StringUtils.isBlank(databaseAi.getAiApiKey()),"AIKey不能为空");
        Preconditions.checkArgument(!StringUtils.isBlank(databaseAi.getAiApiUrl()),"Url不能为空");
        Preconditions.checkArgument(!StringUtils.isBlank(databaseAi.getAiApiModel()),"模型不能为空");
        Preconditions.checkNotNull(databaseAi.getUserId(),"所属用户不能为空");
        User user = userService.queryById(LoginContextHolder.getLoginId());
        System.out.println(user);
        if(!"1".equals(user.getManager())){
            return Result.fail("没有权限");
        }
        int insert  = databaseAiService.insert(databaseAi);
        if(insert>0){
            return Result.ok("插入成功");
        }else {
            return Result.fail("插入失败");
        }
    }
}
