package com.ratelimiter.rate_limiter.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum QuoteStatus {
    ACCEPTED;

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
