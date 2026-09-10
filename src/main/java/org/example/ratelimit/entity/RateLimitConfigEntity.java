package org.example.ratelimit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "rate_limit_config")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RateLimitConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String routeKey;
    private String algorithm;
    private int requestLimit;
    private int windowSeconds;
    private Double refillRatePerSec;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
