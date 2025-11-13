package com.github.frederikpietzko.cloudnativespring.restaurant.infrastructure.outbox

import io.micrometer.core.instrument.binder.MeterBinder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OutboxMetricConfiguration {
    @Bean
    fun outboxMetrics(outboxRepository: OutboxRepository): MeterBinder {
        return MeterBinder { registry ->
            registry.gauge("outbox_events_pending_count", outboxRepository) { repo ->
                repo.countAllByStatus(status = Status.PENDING).toDouble()
            }
        }
    }
}
