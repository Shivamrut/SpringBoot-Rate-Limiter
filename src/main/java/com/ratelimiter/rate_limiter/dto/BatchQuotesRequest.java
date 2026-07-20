package com.ratelimiter.rate_limiter.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BatchQuotesRequest {
    @NotEmpty
    @NotNull
    @Size(max=10,min=1,message = "Batch request cannot contain more than 10 IDs")
    List<String> ids;
}
