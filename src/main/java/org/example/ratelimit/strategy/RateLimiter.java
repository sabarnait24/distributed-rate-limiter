package org.example.ratelimit.strategy;

import org.example.ratelimit.model.RateLimitConfig;

public interface RateLimiter {
    boolean tryAcquire(String redisKey, RateLimitConfig config);
}
