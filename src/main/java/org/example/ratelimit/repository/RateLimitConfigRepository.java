package org.example.ratelimit.repository;

import org.example.ratelimit.entity.RateLimitConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateLimitConfigRepository extends JpaRepository<RateLimitConfigEntity, Long> {
}