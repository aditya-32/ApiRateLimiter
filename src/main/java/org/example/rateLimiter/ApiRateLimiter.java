package org.example.rateLimiter;

import org.example.entity.Request;

interface ApiRateLimiter {
    boolean rateLimit(Request request);
}
