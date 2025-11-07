package com.github.frederikpietzko.cloudnativespring.restaurant.infrastructure.outbox

import com.github.frederikpietzko.cloudnativespring.infrastructure.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import java.time.OffsetDateTime

@Entity
class OutboxEvent(
    val eventType: String,
    val topic: String,
    val payload: String,
    @Enumerated(STRING) var status: Status = Status.PENDING,
    var sentAt: OffsetDateTime? = null,
    var errorMessage: String? = null,
) : BaseEntity()



