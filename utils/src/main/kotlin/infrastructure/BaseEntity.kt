package com.github.frederikpietzko.cloudnativespring.infrastructure

import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime
import java.util.*
import java.util.UUID.randomUUID

@MappedSuperclass
abstract class BaseEntity {
    @Id
    var id: UUID = randomUUID()

    @CreationTimestamp
    var createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    var updatedAt: OffsetDateTime = OffsetDateTime.now()

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is BaseEntity -> false
            else -> id == other.id
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}