package com.ratelimiter.rate_limiter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ratelimiter.rate_limiter.dto.request.CreateQuoteRequest;
import com.ratelimiter.rate_limiter.dto.response.CreateQuoteResponse;
import com.ratelimiter.rate_limiter.dto.response.QuoteResponse;
import com.ratelimiter.rate_limiter.exception.ResourceNotFoundException;
import com.ratelimiter.rate_limiter.mapper.QuoteMapper;
import com.ratelimiter.rate_limiter.service.QuoteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/quotes")
public class QuoteController {

    private final QuoteService quoteService;
    private final QuoteMapper quoteMapper;

    public QuoteController(QuoteService quoteService, QuoteMapper quoteMapper) {
        this.quoteService = quoteService;
        this.quoteMapper = quoteMapper;
    }

    @GetMapping("/random")
    public ResponseEntity<QuoteResponse> getRandomQuote() {
        
        return quoteService.getRandomQuote()
                            .map(quoteMapper::toQuoteResponse)
                            .map(ResponseEntity::ok)
                            .orElseThrow(() -> 
                            new ResourceNotFoundException("No Quotes found."));
    }

    @PostMapping
    public ResponseEntity<CreateQuoteResponse> createQuote(
        @Valid @RequestBody CreateQuoteRequest quote) {
        CreateQuoteResponse response = quoteMapper.toCreateQuoteResponse( 
            quoteService.createQuote(quote.getText(),quote.getAuthor()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuoteResponse> getQuoteById(
        @PathVariable String id
    ){
        return quoteService.getQuoteById(id)
                            .map(quoteMapper::toQuoteResponse)
                            .map(ResponseEntity::ok)
                            .orElseThrow(()->
                        new ResourceNotFoundException("Quote not found: "+id));
    }
    
}
