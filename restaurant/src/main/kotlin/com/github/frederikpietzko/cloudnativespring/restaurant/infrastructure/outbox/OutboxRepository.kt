package com.github.frederikpietzko.cloudnativespring.restaurant.infrastructure.outbox

import org.springframework.data.jpa.repository.JpaRepository

interface OutboxRepository : JpaRepository<OutboxEvent, String> {
    fun findByStatus(status: Status): List<OutboxEvent>
    fun findAllByStatusInOrderByCreatedAtAsc(statuses: List<Status>): List<OutboxEvent>
}