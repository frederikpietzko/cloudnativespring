package com.github.frederikpietzko.cloudnativespring.restaurant.restaurant

interface EventPublisher {
    fun publishEvent(event: Any)
}