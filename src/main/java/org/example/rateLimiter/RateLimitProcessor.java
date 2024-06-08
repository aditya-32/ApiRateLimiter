package org.example.rateLimiter;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.example.Enum.RateLimiterType;
import org.example.entity.RateLimiterProcessorCommand;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class RateLimitProcessor {
    private final Map<RateLimiterType, ApiRateLimiter> typeToRateLimiterMapping;

    public boolean rateLimit(RateLimiterProcessorCommand command) {
        var rateLimiter= typeToRateLimiterMapping.get(command.getRateLimiterType());
        var result = rateLimiter.rateLimit(command.getRequest());
        if (result) {
            System.out.println("Allowed RequestId "+command.getRequest().getHttpRequest().getUuid());
        } else {
            System.out.println("Not Allowed RequestId "+command.getRequest().getHttpRequest().getUuid());
        }
        return result;
    }
}
