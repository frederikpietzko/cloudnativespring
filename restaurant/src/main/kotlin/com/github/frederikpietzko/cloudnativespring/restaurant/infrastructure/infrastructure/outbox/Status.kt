package com.github.frederikpietzko.cloudnativespring.restaurant.infrastructure.infrastructure.outbox

enum class Status {
    PENDING,
    SENT,
    FAILED
}