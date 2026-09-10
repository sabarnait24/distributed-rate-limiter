package org.example.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ratelimit.RateLimitConfigCache;
import org.example.ratelimit.factory.RateLimiterFactory;
import org.example.ratelimit.model.RateLimitConfig;
import org.example.ratelimit.strategy.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiterFactory factory;
    private final RateLimitConfigCache configCache;

    @Autowired
    public RateLimitInterceptor(RateLimiterFactory factory, RateLimitConfigCache configCache) {
        this.factory = factory;
        this.configCache = configCache;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws IOException {
        String routeKey = getRouteKey(req);
        String clientKey = getClientKey(req);
        String redisKey = getRedisKey(clientKey,routeKey);
        RateLimitConfig config = configCache.getCache().get(routeKey);
        RateLimiter rateLimiter = factory.get(config.getAlgorithm());
        boolean result = rateLimiter.tryAcquire(redisKey, config);
        if (!result) {
            res.sendError(429,"Retry-After sometime");
            return false;
        }
        return true;
    }

    private String getClientKey(HttpServletRequest req) {
        String apiKey = req.getHeader("X-API-Key");
        return apiKey != null ? apiKey : req.getRemoteAddr();
    }

    private String getRouteKey(HttpServletRequest req) {
        String pattern = (String) req.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return req.getMethod() + ":" + pattern;
    }

    private String getRedisKey(String clientKey, String routeKey) {
        return  "ratelimit:counter:" + clientKey + ":" + routeKey;
    }

}
