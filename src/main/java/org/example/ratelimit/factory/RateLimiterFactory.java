package org.example.ratelimit.factory;

import org.example.ratelimit.enums.RateLimitAlgorithm;
import org.example.ratelimit.strategy.FixedWindowRateLimiter;
import org.example.ratelimit.strategy.RateLimiter;
import org.example.ratelimit.strategy.SlidingWindowRateLimiter;
import org.example.ratelimit.strategy.TokenBucketRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RateLimiterFactory {

    private final Map<RateLimitAlgorithm, RateLimiter> factory;

    @Autowired
    public RateLimiterFactory(
            FixedWindowRateLimiter fixedWindowRateLimiter,
            SlidingWindowRateLimiter slidingWindowRateLimiter,
            TokenBucketRateLimiter tokenBucketRateLimiter) {

        this.factory = Map.of(
                RateLimitAlgorithm.FIXED_WINDOW, fixedWindowRateLimiter,
                RateLimitAlgorithm.SLIDING_WINDOW, slidingWindowRateLimiter,
                RateLimitAlgorithm.TOKEN_BUCKET, tokenBucketRateLimiter
        );
    }

    public RateLimiter get(RateLimitAlgorithm algo) {
        RateLimiter limiter = factory.get(algo);
        if (limiter == null) {
            throw new IllegalArgumentException("No rate limiter registered for algorithm: " + algo);
        }
        return limiter;
    }
}
