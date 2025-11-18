package com.github.frederikpietzko.cloudnativespring.order.configuration

import io.github.resilience4j.bulkhead.BulkheadConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4jBulkheadConfigurationBuilder
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4jBulkheadProvider
import org.springframework.cloud.circuitbreaker.retry.FrameworkRetryCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.retry.FrameworkRetryConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.retry.RetryPolicy
import org.springframework.util.backoff.ExponentialBackOff
import java.time.Duration

@Configuration
class CircuitBreakerConfiguration {

    @Bean
    fun circuitBreakerCustomizer(): Customizer<Resilience4JCircuitBreakerFactory> = Customizer { factory ->
        factory.configureDefault { id ->
            Resilience4JConfigBuilder(id)
                .timeLimiterConfig(
                    TimeLimiterConfig
                        .custom()
                        .timeoutDuration(Duration.ofSeconds(1))
                        .build()
                )
                .circuitBreakerConfig(
                    CircuitBreakerConfig.custom()
                        .failureRateThreshold(10f)
                        .waitDurationInOpenState(Duration.ofMillis(1000))
                        .slidingWindowSize(2)
                        .build()
                )
                .build()
        }
    }

    @Bean
    fun retryCircuitBreakerCustomizer(): Customizer<FrameworkRetryCircuitBreakerFactory> {
        return Customizer { factory ->
            factory.configureDefault { id ->
                FrameworkRetryConfigBuilder(id)
                    .retryPolicy(
                        RetryPolicy.builder().backOff(
                            ExponentialBackOff(
                                100, 2.0
                            )
                        ).build()
                    )
                    .openTimeout(Duration.ofSeconds(20))
                    .resetTimeout(Duration.ofSeconds(1))
                    .build()
            }
        }
    }

    @Bean
    fun bulkheadCustomizer(): Customizer<Resilience4jBulkheadProvider> = Customizer { provider ->
        provider.configureDefault { id ->
            Resilience4jBulkheadConfigurationBuilder()
                .bulkheadConfig(
                    BulkheadConfig.custom()
                        .maxConcurrentCalls(4)
                        .maxWaitDuration(Duration.ofSeconds(5))
                        .build()
                )
                .build()
        }
    }
}
