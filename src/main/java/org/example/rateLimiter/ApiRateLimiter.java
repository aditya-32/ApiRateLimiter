package org.example.rateLimiter;

import org.example.entity.Request;

public interface ApiRateLimiter {
    boolean rateLimit(Request request);
}
