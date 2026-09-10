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
public class TokenBucketRateLimiter implements RateLimiter{
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> tokenBucketScript;

    @Autowired
    public TokenBucketRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.tokenBucketScript = RedisScript.of(new ClassPathResource("scripts/token_bucket.lua"), Long.class);
    }


    @Override
    public boolean tryAcquire(String redisKey, RateLimitConfig config) {
        int capacity = config.getLimit();
        long now = Instant.now().toEpochMilli();
        Long result = redisTemplate.execute(
                tokenBucketScript,
                List.of(redisKey),
                capacity,
                config.getRefillRatePerSec(),
                now
        );
        return result == 1;
    }
}
