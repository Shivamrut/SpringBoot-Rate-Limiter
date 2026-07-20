package com.ratelimiter.rate_limiter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ratelimiter.rate_limiter.domain.BatchQuotes;
import com.ratelimiter.rate_limiter.domain.Quote;
import com.ratelimiter.rate_limiter.repository.QuoteRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuoteService {

    private final QuoteRepository repository;

    public QuoteService(QuoteRepository repository) {
        this.repository = repository;
    }

    public Optional<Quote> getRandomQuote() {
        return repository.getRandomQuote();
    }

    public Quote createQuote(String text, String author) {
        Quote quote = Quote.createNew(text, author);
        return repository.createQuote(quote);
    }

    public Optional<Quote> getQuoteById(String id) {
        return repository.getQuoteById(id);
    }

    public BatchQuotes fetchQuotes(List<String> ids){
        return repository.fetchQuotes(ids);
    }
}
