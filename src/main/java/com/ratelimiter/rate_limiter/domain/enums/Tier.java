package com.ratelimiter.rate_limiter.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Tier {
    FREE,
    STANDARD,
    PREMIUM;

    @Override
    @JsonValue
    public String toString() {
        return name().toLowerCase();
    }
}
