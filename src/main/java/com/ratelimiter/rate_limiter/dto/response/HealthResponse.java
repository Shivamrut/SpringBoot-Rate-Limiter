package com.ratelimiter.rate_limiter.dto.response;

import java.time.Instant;

import com.ratelimiter.rate_limiter.domain.enums.HealthStatus;

public record HealthResponse(HealthStatus status, Instant timestamp) {
}
