package org.example.rateLimiter;

import com.google.inject.PrivateModule;
import com.google.inject.multibindings.MapBinder;
import lombok.RequiredArgsConstructor;
import org.example.Enum.RateLimiterType;
import org.example.entity.ApiRateLimitConfig;
import org.example.rateLimiter.impl.SlidingWindowRateLimiter;
import org.example.rateLimiter.impl.TokenBucketRateLimiter;

import static com.google.inject.Scopes.SINGLETON;

@RequiredArgsConstructor
public class RateLimiterModule extends PrivateModule {
    private final ApiRateLimitConfig apiRateLimitConfig;

    @Override
    protected void configure() {
        binder().requireExactBindingAnnotations();
        binder().requireAtInjectOnConstructors();
        binder().disableCircularProxies();
        binder().requireExplicitBindings();

        bindRateLimiterInstance(RateLimiterType.SLIDING_WINDOW, SlidingWindowRateLimiter.class);
        bindRateLimiterInstance(RateLimiterType.TOKEN_BUCKET, TokenBucketRateLimiter.class);
        bind(ApiRateLimitConfig.class).toInstance(apiRateLimitConfig);
        bind(RateLimitProcessor.class).in(SINGLETON);

        expose(RateLimitProcessor.class);
    }
    private void bindRateLimiterInstance(RateLimiterType type, Class<? extends ApiRateLimiter> klass) {
        bind(klass).in(SINGLETON);
        getMapBinder().addBinding(type).to(klass).in(SINGLETON);
    }

    private MapBinder<RateLimiterType, ApiRateLimiter> getMapBinder() {
        return MapBinder.newMapBinder(binder(), RateLimiterType.class, ApiRateLimiter.class);
    }
}
