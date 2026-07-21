package com.ratelimiter.rate_limiter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConvertRequest {
    @NotBlank(message = "Text cannot be blank")
    @Size(max = 10000, message = "Text limit exceeded")
    private String text;
    @NotNull(message = "Valid operation is required")
    private ConvertOperation operation;
}
