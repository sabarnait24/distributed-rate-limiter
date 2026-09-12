//package org.example.redis;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.time.Duration;
//
//@Component
//public class RedisClient {
//
//    private final StringRedisTemplate redisTemplate;
//
//    @Autowired
//    public RedisClient(StringRedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public String get(String key) {
//        return redisTemplate.opsForValue().get(key);
//    }
//
//    public void set(String key, String value) {
//        redisTemplate.opsForValue().set(key, value);
//    }
//
//    public void set(String key, String value, Duration ttl) {
//        redisTemplate.opsForValue().set(key, value, ttl);
//    }
//
//    public Long increment(String key) {
//        return redisTemplate.opsForValue().increment(key);
//    }
//
//    public Boolean delete(String key) {
//        return redisTemplate.delete(key);
//    }
//
//    public Boolean expire(String key, Duration ttl) {
//        return redisTemplate.expire(key, ttl);
//    }
//}