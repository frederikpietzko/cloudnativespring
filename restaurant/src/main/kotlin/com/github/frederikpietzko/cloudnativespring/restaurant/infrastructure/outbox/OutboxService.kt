package com.github.frederikpietzko.cloudnativespring.restaurant.infrastructure.outbox

import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.EventPublisher
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.trace.*
import io.opentelemetry.context.Context
import io.opentelemetry.instrumentation.annotations.WithSpan
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper
import java.time.OffsetDateTime

@Service
class OutboxService(
    private val objectMapper: ObjectMapper,
    private val outboxRepository: OutboxRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : EventPublisher {
    companion object {
        private val logger = LoggerFactory.getLogger(OutboxService::class.java)
        private val tracer = GlobalOpenTelemetry.getTracer("OutboxService")
    }

    @WithSpan
    override fun publishEvent(topic: String, event: Any) {
        val spanContext = Span.current().spanContext
        logger.info("Saving Event to Outbox: $event")
        outboxRepository.save(
            OutboxEvent(
                eventType = event::class.qualifiedName!!,
                payload = objectMapper.writeValueAsString(event),
                topic = topic,
                traceId = spanContext.traceId,
                spanId = spanContext.spanId,
            )
        )
    }

    private fun withSpan(unsentEvent: OutboxEvent, block: Span.() -> Unit) {
        val spanContext = SpanContext.createFromRemoteParent(
            unsentEvent.traceId,
            unsentEvent.spanId,
            TraceFlags.getSampled(),
            TraceState.getDefault()
        )
        val context = Context.root().with(Span.wrap(spanContext))
        val span = tracer
            .spanBuilder("OutboxService.processEvent")
            .setParent(context)
            .startSpan()
        try {
            context.makeCurrent().use {
                span.block()
            }
        } finally {
            span.end()
        }
    }

    @Scheduled(fixedRate = 5000)
    fun process() {
        val unsentEvents = outboxRepository.findAllByStatusInOrderByCreatedAtAsc(listOf(Status.PENDING, Status.FAILED))
        for (unsentEvent in unsentEvents) {
            withSpan(unsentEvent) {
                kafkaTemplate.send(unsentEvent.topic, unsentEvent.payload).handle { res, error ->
                    if (error == null) {
                        unsentEvent.sentAt = OffsetDateTime.now()
                        unsentEvent.status = Status.SENT
                        unsentEvent.errorMessage = null
                        outboxRepository.save(unsentEvent)
                    } else {
                        recordException(error)
                        unsentEvent.sentAt = OffsetDateTime.now()
                        unsentEvent.status = Status.FAILED
                        unsentEvent.errorMessage = error.message
                        outboxRepository.save(unsentEvent)
                    }
                }
            }

        }
    }
}
