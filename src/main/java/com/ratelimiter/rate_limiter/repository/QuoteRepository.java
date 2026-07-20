package com.ratelimiter.rate_limiter.repository;

import java.util.Optional;

import com.ratelimiter.rate_limiter.domain.Quote;

public interface QuoteRepository {
    Optional<Quote> getRandomQuote();
    Quote createQuote(Quote quote);
}
