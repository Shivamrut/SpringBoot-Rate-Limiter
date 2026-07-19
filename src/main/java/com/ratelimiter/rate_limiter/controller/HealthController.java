package com.ratelimiter.rate_limiter.controller;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ratelimiter.rate_limiter.dto.HealthResponse;
import com.ratelimiter.rate_limiter.dto.HealthStatus;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<HealthResponse> checkHealth() {
        try {
            HealthResponse response = new HealthResponse(HealthStatus.UP, Instant.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            HealthResponse response = new HealthResponse(HealthStatus.DOWN, Instant.now());
            return ResponseEntity.status(503).body(response);
        }
    }
}
