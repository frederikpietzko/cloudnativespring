package com.github.frederikpietzko.cloudnativespring.restaurant.infrastructure.outbox

import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.EventPublisher
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
    }

    override fun publishEvent(topic: String, event: Any) {
        logger.info("Saving Event to Outbox: $event")
        outboxRepository.save(
            OutboxEvent(
                eventType = event::class.qualifiedName!!,
                payload = objectMapper.writeValueAsString(event),
                topic = topic,
            )
        )
    }

    @Scheduled(fixedRate = 5)
    fun process() {
        val unsentEvents = outboxRepository.findAllByStatusInOrderByCreatedAtAsc(listOf(Status.PENDING, Status.FAILED))
        for (unsentEvent in unsentEvents) {
            kafkaTemplate.send(unsentEvent.topic, unsentEvent.payload).handle { res, error ->
                if (error == null) {
                    unsentEvent.sentAt = OffsetDateTime.now()
                    unsentEvent.status = Status.SENT
                    unsentEvent.errorMessage = null
                    outboxRepository.save(unsentEvent)
                } else {
                    unsentEvent.sentAt = OffsetDateTime.now()
                    unsentEvent.status = Status.FAILED
                    unsentEvent.errorMessage = error.message
                    outboxRepository.save(unsentEvent)
                }
            }
        }
    }
}
