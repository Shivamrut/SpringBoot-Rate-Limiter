package com.ratelimiter.rate_limiter.web;

public final class ApiConstants {

    private ApiConstants() {
    }

    public static final class Headers {
        public static final String API_KEY = "X-API-Key";
        public static final String REQUEST_ID = "X-Request-Id";

        private Headers() {
        }
    }

    public static final class Attributes {
        public static final String REQUEST_ID = "requestId";
        public static final String CLIENT = "client";

        private Attributes() {
        }
    }
}
