//package org.example.redis;
//
//import org.springframework.stereotype.Repository;
//
//import java.time.Duration;
//
//@Repository
//public class RedisRateLimitRepository {
//
//    private final RedisClient redisClient;
//
//    public RedisRateLimitRepository(RedisClient redisClient) {
//        this.redisClient = redisClient;
//    }
//
//    public long incrementRequestCount(String key) {
//        Long count = redisClient.increment(key);
//        return count != null ? count : 0;
//    }
//
//    public void setExpiry(String key, Duration ttl) {
//        redisClient.expire(key, ttl);
//    }
//
//    public String get(String key) {
//        return redisClient.get(key);
//    }
//}