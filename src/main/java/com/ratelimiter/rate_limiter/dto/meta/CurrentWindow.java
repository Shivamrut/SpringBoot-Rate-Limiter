package com.ratelimiter.rate_limiter.dto.meta;

import java.time.Instant;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentWindow {
    private Instant windowStart;
    private Instant windowEnd;
    private Map<String,EndpointQuota> endpoints;
}
