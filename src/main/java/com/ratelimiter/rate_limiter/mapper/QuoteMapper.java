package com.ratelimiter.rate_limiter.mapper;

import org.springframework.stereotype.Component;

import com.ratelimiter.rate_limiter.domain.Quote;
import com.ratelimiter.rate_limiter.dto.CreateQuoteResponse;
import com.ratelimiter.rate_limiter.dto.QuoteResponse;
import com.ratelimiter.rate_limiter.dto.QuoteStatus;

@Component
public class QuoteMapper {
    public QuoteResponse toQuoteResponse(Quote quote) {
        
        return QuoteResponse.builder()
                            .author(quote.getAuthor())
                            .text(quote.getText())
                            .id(quote.getId())
                            .build();
        
    }
    public CreateQuoteResponse toCreateQuoteResponse(Quote quote) {
        
        return CreateQuoteResponse.builder()
                            .author(quote.getAuthor())
                            .text(quote.getText())
                            .id(quote.getId())
                            .status(QuoteStatus.ACCEPTED)
                            .build();
    }
}
