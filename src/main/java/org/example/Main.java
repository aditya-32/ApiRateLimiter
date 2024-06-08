package org.example;

import com.google.inject.Guice;
import com.google.inject.Stage;
import org.example.Enum.RateLimiterType;
import org.example.entity.ApiRateLimitConfig;
import org.example.entity.HttpRequest;
import org.example.entity.RateLimiterProcessorCommand;
import org.example.entity.Request;
import org.example.rateLimiter.RateLimitProcessor;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var apiRateLimitConfig = ApiRateLimitConfig.builder()
                .rateLimit(10)
                .timeUnit(ChronoUnit.SECONDS)
                .build();
        var injector = Guice.createInjector(Stage.PRODUCTION, new ApplicationModule(apiRateLimitConfig));
        var apiRateLimiterProcessor = injector.getInstance(RateLimitProcessor.class);

        List<Callable<Boolean>> callables = new ArrayList<>();
        for (int i = 1 ; i <= 20 ; i++) {
            var command = RateLimiterProcessorCommand.builder()
                    .rateLimiterType(RateLimiterType.SLIDING_WINDOW)
                    .request(Request.builder()
                            .httpRequest(HttpRequest.builder().uuid(i).build())
                            .build()).build();
            callables.add(() -> apiRateLimiterProcessor.rateLimit(command));
        }
        var executor = Executors.newFixedThreadPool(20);
        List<Future<Boolean>> futures = executor.invokeAll(callables);
        for (var future : futures) {
            future.get();
        }
        Thread.sleep(2000);
        for (int i = 21 ; i <= 40 ; i++) {
            var command = RateLimiterProcessorCommand.builder()
                    .rateLimiterType(RateLimiterType.TOKEN_BUCKET)
                    .request(Request.builder()
                            .httpRequest(HttpRequest.builder().uuid(i).build())
                            .build()).build();
            callables.add(() -> apiRateLimiterProcessor.rateLimit(command));
        }
        executor = Executors.newFixedThreadPool(20);
        futures = executor.invokeAll(callables);
        for (var future : futures) {
            future.get();
        }
    }
}