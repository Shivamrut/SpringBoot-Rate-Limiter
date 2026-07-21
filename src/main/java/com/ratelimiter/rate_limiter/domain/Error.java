package com.ratelimiter.rate_limiter.domain;

public record Error (String code, String message, String requestId) {}
