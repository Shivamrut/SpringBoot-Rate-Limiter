package com.ratelimiter.rate_limiter.domain;

import com.ratelimiter.rate_limiter.domain.enums.Tier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private String clientId;
    private String apiKey;
    private Tier tier;

}
