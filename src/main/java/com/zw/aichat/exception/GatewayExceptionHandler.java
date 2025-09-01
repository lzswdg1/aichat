package com.zw.aichat.exception;

import cn.dev33.satoken.exception.SaTokenException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zw.aichat.bean.Result;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static cn.dev33.satoken.SaManager.log;

@Component
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable){
        log.error("网关层面捕获到异常", throwable);
        throwable.printStackTrace();
        ServerHttpRequest request =serverWebExchange.getRequest();
        ServerHttpResponse response = serverWebExchange.getResponse();
        Integer code = 200;
        String msg = "";
        if(throwable instanceof SaTokenException){
            code = 401;
            msg = "用户无权限";
        }else {
            code = 500;
            msg= "系统繁忙";
        }
        Result fail = Result.fail(code,msg);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.fromSupplier(()->{
            DataBufferFactory bufferFactory = response.bufferFactory();
            byte[] bytes = null;
            try {
                bytes = objectMapper.writeValueAsBytes(fail);
            }catch (JsonProcessingException e){
                e.printStackTrace();
            }
            return bufferFactory.wrap(bytes);
        }));
    }
}
