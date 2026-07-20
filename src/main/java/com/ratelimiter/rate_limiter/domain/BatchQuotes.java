package com.ratelimiter.rate_limiter.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchQuotes {
    private List<Quote> quotes;
    List<String> notFound;
    int found;
}
