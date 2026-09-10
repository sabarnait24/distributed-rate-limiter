package org.example.ratelimit.strategy;

import org.example.ratelimit.model.RateLimitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class SlidingWindowRateLimiter implements RateLimiter{
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> slidingWindowScript;

    @Autowired
    public SlidingWindowRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.slidingWindowScript = RedisScript.of(new ClassPathResource("scripts/sliding_window.lua"), Long.class);
    }


    @Override
    public boolean tryAcquire(String redisKey, RateLimitConfig config) {
        int limit = config.getLimit();
        int windowSeconds = config.getWindowSeconds();
        long currentTime = Instant.now().toEpochMilli();
        Long result = redisTemplate.execute(
                slidingWindowScript,
                List.of(redisKey),
                limit,
                windowSeconds,
                currentTime
        );
        return result == 1;
    }
}
