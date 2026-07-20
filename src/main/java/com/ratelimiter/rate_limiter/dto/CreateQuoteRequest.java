package com.ratelimiter.rate_limiter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateQuoteRequest {
    @NotBlank(message = "Quote text can not be blank")
    @Size(min = 10, max = 500, message = "Quote text must be between 10 and 500 characters long")
    private String text;
    @NotBlank(message = "Quote author can not be blank")
    @Size(min = 1, max = 100, message = "Author name must be between 1 and 100 characters long")
    private String author;
}
