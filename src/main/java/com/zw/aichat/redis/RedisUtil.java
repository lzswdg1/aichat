package com.zw.aichat.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class RedisUtil {
    @Resource
    private RedisTemplate redisTemplate;

    private static final String CACHE_KEY_SEPARATOR = ".";

    //构建缓存key

    public String buildKey(String... strObjs){
        return  Stream.of(strObjs).collect(Collectors.joining(CACHE_KEY_SEPARATOR));
    }

    //是否存在key
    public Boolean exist(String key) {
        return redisTemplate.hasKey(key);
    }

    //删除key
    public Boolean del(String key){
        return redisTemplate.delete(key);
    }

    //不带过期设置key value
    public void set(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    public Boolean setNx(String key, String value, Long time, TimeUnit timeUnit){
        return redisTemplate.opsForValue().setIfAbsent(key, value, time, timeUnit);
    }

    public String get(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    public  Boolean zAdd(String key,String value,Long score){
        return redisTemplate.opsForZSet().add(key, value, Double.valueOf(score));
    }

    public Long countZset(String key) {
        return redisTemplate.opsForZSet().size(key);
    }
    public Set<String> rangeZset(String key,long start,long end){
        return redisTemplate.opsForZSet().range(key, start, end);
    }
    public Long removeZset(String key,Object value){
        return redisTemplate.opsForZSet().remove(key, value);
    }
    public void removeZsetList(String key,Set<Object> values){
        values.stream().forEach((val)-> redisTemplate.opsForZSet().remove(key,val));
    }
    public Double score(String key,Object value){
        return redisTemplate.opsForZSet().score(key, value);
    }
    public Set<String> rangeByScore(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeByScore(key, Double.valueOf(String.valueOf(start)), Double.valueOf(String.valueOf(end)));
    }
    public Object addScore(String key,Object value,Double score){
        return redisTemplate.opsForZSet().incrementScore(key, value, Double.valueOf(score));
    }
    public Object rank(String key, Object obj) {
        return redisTemplate.opsForZSet().rank(key, obj);
    }

    public Set<ZSetOperations.TypedTuple<String>> rankWithScore(String key, long start, long end){
        //它用于从 Redis 中获取一个有序集合（sorted set）中指定排名范围的成员及其分数，并将结果返回为一个 Set 集合
        return (Set<ZSetOperations.TypedTuple<String>>) redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }
    public void putHash(String key, String hashKey, Object hashValue){
        redisTemplate.opsForHash().put(key, hashKey, hashValue);
    }

    public Integer getInt(String key){
        return (Integer) redisTemplate.opsForValue().get(key);
    }

    public void increment(String key, Integer count){
        redisTemplate.opsForValue().increment(key, count);
    }
    public Map<Object, Object> getHashAndDelete(String key) {
        Map<Object, Object> map = new HashMap<>();
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key, ScanOptions.NONE);
        while(cursor.hasNext()){
            Map.Entry<Object, Object> entry = cursor.next();
            Object hashKey = entry.getKey();
            Object value = entry.getValue();
            map.put(hashKey, value);
            redisTemplate.opsForHash().delete(key, hashKey);
        }
        return map;
    }
}
