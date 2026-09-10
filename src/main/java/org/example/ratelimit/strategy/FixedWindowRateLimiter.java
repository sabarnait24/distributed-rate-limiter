package org.example.ratelimit.strategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.ratelimit.model.RateLimitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter

public class FixedWindowRateLimiter implements RateLimiter{
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> fixedWindowScript;

    @Autowired
    public FixedWindowRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.fixedWindowScript = RedisScript.of(new ClassPathResource("scripts/fixed_window.lua"), Long.class);
    }


    @Override
    public boolean tryAcquire(String redisKey, RateLimitConfig config) {
        int limit = config.getLimit();
        int windowSeconds = config.getWindowSeconds();
        Long result = redisTemplate.execute(
                fixedWindowScript,
                List.of(redisKey),
                limit,
                windowSeconds
        );
        return result == 1;
    }
}
