package com.ratelimiter.rate_limiter.repository;

import com.ratelimiter.rate_limiter.domain.Client;

public interface ClientRepository {
    public Client getClient(String apiKey);
}
