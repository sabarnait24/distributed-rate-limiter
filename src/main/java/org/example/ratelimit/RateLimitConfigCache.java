package org.example.ratelimit;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.ratelimit.entity.RateLimitConfigEntity;
import org.example.ratelimit.enums.RateLimitAlgorithm;
import org.example.ratelimit.model.RateLimitConfig;
import org.example.ratelimit.repository.RateLimitConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Getter
public class RateLimitConfigCache {

    private final RateLimitConfigRepository repository;
    private final Map<String, RateLimitConfig> cache = new ConcurrentHashMap<>();
    private static final RateLimitConfig DEFAULT =
            new RateLimitConfig("default", RateLimitAlgorithm.FIXED_WINDOW, 50, 60, null);

    public RateLimitConfigCache(RateLimitConfigRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        refresh();
    }

    @Scheduled(fixedDelayString = "${ratelimit.refresh.ms:60000}")
    public void refresh() {
        log.info("========== RATE LIMIT REFRESH ==========");
        List<RateLimitConfigEntity> entities = repository.findAll();
        entities.forEach(e -> {
            log.info(
                    "route={} limit={}",
                    e.getRouteKey(),
                    e.getRequestLimit()
            );

            cache.put(e.getRouteKey(), toModel(e));
        });
    }

    public RateLimitConfig resolve(String routeKey) {
        return cache.getOrDefault(routeKey, DEFAULT);
    }

    private RateLimitConfig toModel(RateLimitConfigEntity e) {
        RateLimitAlgorithm algo = RateLimitAlgorithm.valueOf(e.getAlgorithm());
        if (algo == RateLimitAlgorithm.TOKEN_BUCKET && e.getRefillRatePerSec() == null) {
            throw new IllegalStateException("refillRatePerSec required for TOKEN_BUCKET: " + e.getRouteKey());
        }
        return new RateLimitConfig(e.getRouteKey(), algo, e.getRequestLimit(), e.getWindowSeconds(), e.getRefillRatePerSec());
    }
}