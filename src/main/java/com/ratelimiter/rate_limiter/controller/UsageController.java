package com.ratelimiter.rate_limiter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ratelimiter.rate_limiter.domain.Client;
import com.ratelimiter.rate_limiter.dto.response.UsageResponse;
import com.ratelimiter.rate_limiter.service.UsageService;
import com.ratelimiter.rate_limiter.web.ApiConstants;

@RestController
@RequestMapping("/v1/usage")
public class UsageController {

    private final UsageService service;

    public UsageController(UsageService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<UsageResponse> getUsage(
        @RequestAttribute(ApiConstants.Attributes.CLIENT) Client client
    ){
        UsageResponse response = service.getUsage(client);
        return ResponseEntity.ok(response);
    }
}
