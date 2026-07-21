package com.ratelimiter.rate_limiter.dto.response;

import com.ratelimiter.rate_limiter.domain.enums.QuoteStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuoteResponse extends QuoteResponse {
    private QuoteStatus status;
}
