package com.ratelimiter.rate_limiter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ratelimiter.rate_limiter.dto.request.BatchQuotesRequest;
import com.ratelimiter.rate_limiter.dto.response.BatchQuotesResponse;
import com.ratelimiter.rate_limiter.mapper.QuoteMapper;
import com.ratelimiter.rate_limiter.service.QuoteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/batch")
public class BatchQuoteController {
    private final QuoteService quoteService;
    private final QuoteMapper quoteMapper;

    public BatchQuoteController(QuoteService quoteService, QuoteMapper quoteMapper) {
        this.quoteService = quoteService;
        this.quoteMapper = quoteMapper;
    }
    @PostMapping("/quotes")
    public ResponseEntity<BatchQuotesResponse> fetchQuotes(
        @RequestBody 
        @Valid
        BatchQuotesRequest request){

        return ResponseEntity.ok(
            quoteMapper.toBatchQuotesResponse( 
                quoteService.fetchQuotes(request.getIds())));
    }
}
