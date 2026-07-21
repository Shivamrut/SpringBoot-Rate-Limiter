package com.ratelimiter.rate_limiter.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ConvertOperation {
    REVERSE,
    UPPERCASE,
    WORDCOUNT;

    @Override
    @JsonValue
    public String toString(){
        return name().toLowerCase();
    }

    @JsonCreator
    public static ConvertOperation fromString(String value) {
        if (value == null){
            return null;
        }
        try {
            return ConvertOperation.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported operation: " + value);
        }
    }
}
