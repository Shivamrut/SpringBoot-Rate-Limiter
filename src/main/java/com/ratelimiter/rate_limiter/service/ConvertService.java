package com.ratelimiter.rate_limiter.service;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.ratelimiter.rate_limiter.domain.enums.ConvertOperation;

@Service
public class ConvertService {
    public String convertText(ConvertOperation operation, String text) {
        try{
            Thread.sleep( ThreadLocalRandom.current().nextLong(200,301));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Convert Interrupted");
        }
        if(operation.equals(ConvertOperation.REVERSE)){
            return new StringBuilder(text).reverse().toString();
        }
        else if(operation.equals(ConvertOperation.UPPERCASE)){
            return text.toUpperCase();
        }
        else {
            return String.valueOf(text.split("\\s+").length);
        }
    }
}
