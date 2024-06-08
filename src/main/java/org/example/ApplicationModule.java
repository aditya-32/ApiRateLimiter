package org.example;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import org.example.entity.ApiRateLimitConfig;
import org.example.rateLimiter.RateLimiterModule;

@RequiredArgsConstructor
public class ApplicationModule extends AbstractModule {
    private final ApiRateLimitConfig apiRateLimitConfig;

    @Override
    protected void configure() {
        binder().requireExactBindingAnnotations();
        binder().requireAtInjectOnConstructors();
        binder().disableCircularProxies();
        binder().requireExplicitBindings();
        install(new RateLimiterModule(apiRateLimitConfig));
    }
}
