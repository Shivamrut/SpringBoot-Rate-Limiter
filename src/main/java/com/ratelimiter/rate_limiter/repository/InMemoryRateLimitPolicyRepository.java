package com.ratelimiter.rate_limiter.repository;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.ratelimiter.rate_limiter.domain.RateLimitPolicy;

@Repository
public class InMemoryRateLimitPolicyRepository implements RateLimitRepository {
    private final Map<String,RateLimitPolicy> policies;

    public InMemoryRateLimitPolicyRepository() {
        policies = new ConcurrentHashMap<>();
        loadPolicy();
    }

    private void loadPolicy() {
        policies.put("GET /v1/quotes/random", new RateLimitPolicy(
            "GET /v1/quotes/random", 30, 100, 500, false
        ));
        policies.put("GET /v1/quotes/{id}", new RateLimitPolicy(
            "GET /v1/quotes/{id}", 30, 100, 500, false
        ));
        policies.put("POST /v1/quotes", new RateLimitPolicy(
            "POST /v1/quotes", 5, 20, 100, false
        ));
        policies.put("POST /v1/convert", new RateLimitPolicy(
            "POST /v1/convert", 5, 15, 50, false
        ));
        policies.put("POST /v1/batch/quotes", new RateLimitPolicy(
            "POST /v1/batch/quotes", 10, 30, 100, false
        ));
        policies.put("GET /v1/usage", new RateLimitPolicy(
            "GET /v1/usage", 10, 30, 60, false
        ));
        policies.put("GET /health", new RateLimitPolicy(
            "GET /health", 0, 0, 0, true
        ));
    }

    @Override
    public Map<String, RateLimitPolicy> getPolicies() {
        return Collections.unmodifiableMap(policies);
    }
    
    
}
