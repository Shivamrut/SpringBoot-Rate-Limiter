package com.ratelimiter.rate_limiter.domain;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quote {
    private static final AtomicInteger idCounter = new AtomicInteger(1050);
    private String id;
    private String text;
    private String author;
    private Instant createdAt;

    public static Quote createNew(String text, String author) {
        int nextId = idCounter.incrementAndGet();
        return new Quote(
            "q-" + nextId,
            text,
            author,
            Instant.now()
        );
    }
}
