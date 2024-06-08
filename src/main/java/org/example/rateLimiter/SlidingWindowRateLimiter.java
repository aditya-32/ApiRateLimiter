package org.example.rateLimiter;

import com.google.inject.Inject;
import java.util.ArrayDeque;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.example.entity.ApiRateLimitConfig;
import org.example.entity.Request;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class SlidingWindowRateLimiter implements ApiRateLimiter{
    private final Queue<Request> requestSequenceList = new ArrayDeque<>();
    private final ApiRateLimitConfig apiRateLimitConfig;
    @Override
    public boolean rateLimit(Request request) {
        var timeStamp = request.getTimeStamp();
        var configTimeUnit =apiRateLimitConfig.getTimeUnit();
        var timeThreshold = timeStamp.minus(1, configTimeUnit);
        synchronized (requestSequenceList) {
            while(CollectionUtils.isNotEmpty(requestSequenceList) && requestSequenceList.peek().getTimeStamp().isBefore(timeThreshold)) {
                var removedRequest = requestSequenceList.poll();
                var httpRequest = removedRequest.getHttpRequest();
                System.out.println("RemovedRequest "+ httpRequest.getUuid() +" "+ removedRequest.getTimeStamp());
            }
            if (requestSequenceList.size() < apiRateLimitConfig.getRateLimit()) {
                requestSequenceList.add(request);
                return true;
            }
        }
        return false;
    }
}
