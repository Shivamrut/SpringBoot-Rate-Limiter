package com.ratelimiter.rate_limiter.dto.response;

import com.ratelimiter.rate_limiter.domain.enums.ConvertOperation;

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
