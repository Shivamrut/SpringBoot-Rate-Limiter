package com.ratelimiter.rate_limiter.dto.response;

import com.ratelimiter.rate_limiter.domain.Error;

public record ErrorResponse (Error error){
    public static ErrorResponse of(String code, String message, String requestId){
        return new ErrorResponse(new Error(code,message,requestId));
    }
}
