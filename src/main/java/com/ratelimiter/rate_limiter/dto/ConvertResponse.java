package com.ratelimiter.rate_limiter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvertResponse {
    private ConvertOperation operation;
    private String input;
    private String result;
    private long processingTimeMs;
}
