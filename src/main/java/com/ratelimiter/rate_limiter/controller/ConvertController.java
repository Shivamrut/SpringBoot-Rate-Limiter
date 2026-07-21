package com.ratelimiter.rate_limiter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ratelimiter.rate_limiter.dto.ConvertRequest;
import com.ratelimiter.rate_limiter.dto.ConvertResponse;
import com.ratelimiter.rate_limiter.service.ConvertService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/convert")
public class ConvertController {

    private final ConvertService convertService;

    public ConvertController(ConvertService convertService){
        this.convertService = convertService;
    }

    @PostMapping
    public ResponseEntity<ConvertResponse> convertText(
        @RequestBody
        @Valid
        ConvertRequest request
    ){
        long startTime = System.currentTimeMillis();
        String result = convertService.convertText(request.getOperation(), request.getText());
        long processingTimeMs = System.currentTimeMillis() - startTime ;
        return ResponseEntity.ok(
            ConvertResponse.builder()
            .input(request.getText())
            .result(result)
            .processingTimeMs(processingTimeMs)
            .operation(request.getOperation())
            .build()
        );
    }
}
