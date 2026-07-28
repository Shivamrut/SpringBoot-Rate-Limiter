package com.ratelimiter.rate_limiter.dto.meta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointQuota {
    private int limit;
    private int used;
    private int remaining;
}
