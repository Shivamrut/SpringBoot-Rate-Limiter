package com.ratelimiter.rate_limiter.service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ratelimiter.rate_limiter.domain.Client;
import com.ratelimiter.rate_limiter.domain.RateLimitPolicy;
import com.ratelimiter.rate_limiter.dto.meta.CurrentWindow;
import com.ratelimiter.rate_limiter.dto.meta.EndpointQuota;
import com.ratelimiter.rate_limiter.dto.response.UsageResponse;
import com.ratelimiter.rate_limiter.repository.RateLimitRepository;

@Service
public class UsageService {
    
    private final RateLimitRepository repository;

    public UsageService(RateLimitRepository repository){
        this.repository = repository;
    }

    public UsageResponse getUsage(Client client){

        Map<String,RateLimitPolicy> policies = repository.getPolicies();

        UsageResponse response = new UsageResponse();
        response.setClientId(client.getClientId());
        response.setTier(client.getTier());

        Map<String,EndpointQuota> quotas = new LinkedHashMap<>();
        policies.entrySet().stream()
        .forEach(entry -> {
            String endpoint = entry.getKey();
            RateLimitPolicy policy = entry.getValue();
            if(policy.exempt()) return;
            EndpointQuota quota = new EndpointQuota();
            quota.setUsed(0);
            int limit = policy.limitFor(client.getTier());
            quota.setLimit(limit);  
            quota.setRemaining(limit-quota.getUsed());
            quotas.put(endpoint, quota);
        });

        Instant windowStart = Instant.now();
        Instant windowEnd = windowStart.plusSeconds(60);
        CurrentWindow window = new CurrentWindow(windowStart,windowEnd,quotas);
        response.setCurrentWindow(window); 

        return response;
    }
}
