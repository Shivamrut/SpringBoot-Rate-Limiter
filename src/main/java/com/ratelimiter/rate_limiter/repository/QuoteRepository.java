package com.ratelimiter.rate_limiter.repository;

import java.util.List;
import java.util.Optional;

import com.ratelimiter.rate_limiter.domain.BatchQuotes;
import com.ratelimiter.rate_limiter.domain.Quote;

public interface QuoteRepository {
    Optional<Quote> getRandomQuote();
    Quote createQuote(Quote quote);
    Optional<Quote> getQuoteById(String id);
    BatchQuotes fetchQuotes(List<String> ids);
}
