package com.ratelimiter.rate_limiter.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ratelimiter.rate_limiter.Services.QuotesService;

@RestController
@RequestMapping("/v1/quotes")
public class QuotesController {
    
    private final QuotesService handler;

    public QuotesController(QuotesService handler){
        this.handler = handler;
    }

    @GetMapping("/")
    public String getQuote(){
        return handler.getQuote();
    }
}
