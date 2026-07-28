package com.ratelimiter.rate_limiter.domain;

import com.ratelimiter.rate_limiter.domain.enums.Tier;

public record RateLimitPolicy(
    String endpointKey,
    int freeLimit,
    int standardLimit,
    int premiumLimit,
    boolean exempt
) {
    public int limitFor(Tier tier) {
        switch (tier) {
            case FREE:
                return freeLimit;
            case STANDARD:
                return standardLimit;
            case PREMIUM:
                return premiumLimit;
            default:
                return 0;
        }
    }
}
