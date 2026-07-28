package com.ratelimiter.rate_limiter.dto.response;


import com.ratelimiter.rate_limiter.domain.enums.Tier;
import com.ratelimiter.rate_limiter.dto.meta.CurrentWindow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageResponse {
    private String clientId;
    private Tier tier;
    private CurrentWindow currentWindow;
}
