package com.ratelimiter.rate_limiter.repository;

import java.util.Map;

import com.ratelimiter.rate_limiter.domain.RateLimitPolicy;

public interface RateLimitRepository {
    public Map<String,RateLimitPolicy> getPolicies();
}
