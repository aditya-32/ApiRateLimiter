package org.example.rateLimiter;

import jakarta.inject.Inject;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.example.entity.ApiRateLimitConfig;
import org.example.entity.Request;

public class TokenBucketRateLimiter implements ApiRateLimiter {
    private final AtomicInteger currentCapacity;
    private final ApiRateLimitConfig apiRateLimitConfig;
    private final AtomicLong lastUpdatedTime;


    @Inject
    TokenBucketRateLimiter(ApiRateLimitConfig apiRateLimitConfig) {
        this.lastUpdatedTime = new AtomicLong(Instant.now().toEpochMilli());
        this.apiRateLimitConfig = apiRateLimitConfig;
        this.currentCapacity = new AtomicInteger(apiRateLimitConfig.getRateLimit());
    }

    @Override
    public boolean rateLimit(Request request) {
        return tryGetToken(request);
    }

    private synchronized boolean tryGetToken(Request request) {
        refreshTokens(request);
        if (currentCapacity.get() > 0) {
            currentCapacity.decrementAndGet();
            return true;
        }
        return false;
    }

    private void refreshTokens(Request request) {
        var requestTime = request.getTimeStamp();
        var refreshRate = apiRateLimitConfig.getRateLimit();
        var timeUnit = apiRateLimitConfig.getTimeUnit();
        var timeDiff = timeUnit.between(requestTime, Instant.ofEpochMilli(lastUpdatedTime.get()));
        var additionTokens = refreshRate * timeDiff;
        var newCapacity = currentCapacity.get() + additionTokens;
        currentCapacity.getAndSet((int) newCapacity);
    }
}
