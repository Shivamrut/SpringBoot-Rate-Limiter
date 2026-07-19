package com.ratelimiter.rate_limiter.dto;

import java.time.Instant;

public record HealthResponse(HealthStatus status, Instant timestamp) {
}
