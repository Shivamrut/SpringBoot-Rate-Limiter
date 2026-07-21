package com.ratelimiter.rate_limiter.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchQuotesResponse {
    private List<QuoteResponse> results;
    private int found;
    private int notFound;
}
