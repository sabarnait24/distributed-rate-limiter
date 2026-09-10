package org.example.ratelimit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.ratelimit.enums.RateLimitAlgorithm;
@Data
@AllArgsConstructor
public class RateLimitConfig {
    private String routeKey;
    private RateLimitAlgorithm algorithm;
    private int limit;
    private int windowSeconds;
    private Double refillRatePerSec;

}