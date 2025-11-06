package com.github.frederikpietzko.cloudnativespring.restaurant.infrastructure.infrastructure.outbox

import com.github.frederikpietzko.cloudnativespring.infrastructure.BaseEntity
import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.EventPublisher
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper
import java.time.OffsetDateTime
import kotlin.reflect.KClass

@Entity
class OutboxEvent(
    val eventType: String,
    val payload: String,
    @Enumerated(STRING) var status: Status = Status.PENDING,
    var sentAt: OffsetDateTime? = null,
    var errorMessage: String? = null,
) : BaseEntity()

data class EventMapping(
    val eventClass: String,
    val topic: String,
)

data class EventMappings(
    val mappings: MutableList<EventMapping>,
) {
    fun addMapping(eventClass: KClass<*>, topic: String) {
        mappings.add(EventMapping(eventClass.qualifiedName!!, topic))
    }
}

fun eventMappings(initializer: EventMappings.() -> Unit): EventMappings {
    val eventMappings = EventMappings(mutableListOf())
    eventMappings.initializer()
    return eventMappings
}

val eventMappings = eventMappings {
    addMapping(Any::class, "RestaurantRegistedEvent")
}

@Service
class OutboxService(
    private val objectMapper: ObjectMapper,
    private val outboxRepository: OutboxRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : EventPublisher {
    companion object {
        private val logger = LoggerFactory.getLogger(OutboxService::class.java)
    }

    override fun publishEvent(event: Any) {
        logger.info("Saving Event to Outbox: $event")
        outboxRepository.save(
            OutboxEvent(
                eventType = event::class.qualifiedName!!,
                payload = objectMapper.writeValueAsString(event),
            )
        )
    }

    fun process() {
        val unsentEvents = outboxRepository.findAllByStatusInOrderByCreatedAtAsc(listOf(Status.PENDING, Status.FAILED))
        for (unsentEvent in unsentEvents) {
            val topics = eventMappings.mappings.filter { it.eventClass == unsentEvent.eventType }
            for (topic in topics) {
                kafkaTemplate.send(topic.topic, unsentEvent.payload).handle { res, error ->
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
}
