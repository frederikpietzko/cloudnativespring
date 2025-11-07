package com.github.frederikpietzko.cloudnativespring.restaurant.infrastructure.outbox

enum class Status {
    PENDING,
    SENT,
    FAILED
}