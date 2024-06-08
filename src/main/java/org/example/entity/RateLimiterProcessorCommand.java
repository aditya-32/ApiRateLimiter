package org.example.entity;

import lombok.Builder;
import lombok.Value;
import org.example.Enum.RateLimiterType;

@Value
@Builder
public class RateLimiterProcessorCommand {
    Request request;
    RateLimiterType rateLimiterType;
}
