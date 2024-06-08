package org.example.entity;

import lombok.Builder;
import lombok.Value;

import java.time.temporal.ChronoUnit;

@Value
@Builder
public class ApiRateLimitConfig {
    Integer rateLimit;
    ChronoUnit timeUnit;
}
