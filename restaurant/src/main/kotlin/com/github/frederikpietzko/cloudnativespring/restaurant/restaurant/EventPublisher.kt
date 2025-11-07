package com.github.frederikpietzko.cloudnativespring.restaurant.restaurant

interface EventPublisher {
    fun publishEvent(topic: String, event: Any)
}