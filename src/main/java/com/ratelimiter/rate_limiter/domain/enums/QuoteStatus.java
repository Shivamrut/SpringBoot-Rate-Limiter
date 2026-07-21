package com.ratelimiter.rate_limiter.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum QuoteStatus {
    ACCEPTED;

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
