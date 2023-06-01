package com.biuqu.boot.configure;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 熔断降级配置器
 *
 * @author BiuQu
 * @date 2023/3/19 06:58
 */
@Slf4j
@Configuration
public class CircuitBreakerConfigurer
{
    /**
     * 全局默认的策略断路器
     *
     * @param circuitRegistry 断路器注册器
     * @return 断路器
     */
    @Primary
    @Bean
    public CircuitBreaker globalCircuitBreaker(CircuitBreakerRegistry circuitRegistry)
    {
        CircuitBreakerConfig circuitBreakerConf = circuitRegistry.getDefaultConfig();
        log.info("circuit breaker config is:{}", circuitBreakerConf);
        CircuitBreaker circuitBreaker = circuitRegistry.circuitBreaker("default", circuitBreakerConf);

        circuitBreaker.getEventPublisher().onSuccess(event -> log.info("circuit breaker success：{}", event))
            .onError(event -> log.info("circuit breaker error：{}", event))
            .onIgnoredError(event -> log.info("circuit breaker ignore：{}", event))
            .onReset(event -> log.info("circuit breaker reset：{}", event))
            .onStateTransition(event -> log.info("circuit breaker transition：{}", event))
            .onCallNotPermitted(event -> log.info("circuit breaker not permitted：{}", event));

        return circuitBreaker;
    }

    /**
     * 全局默认的时间策略(慢请求)断路器
     * <p>
     * 当前仅支持Netty(在servlet的服务中没有意义)
     *
     * @param timeRegistry 断路器注册器
     * @return 断路器
     */
    @Primary
    @Bean
    public TimeLimiter globalTimeLimiter(TimeLimiterRegistry timeRegistry)
    {
        return timeRegistry.timeLimiter("default", timeRegistry.getDefaultConfig());
    }
}
