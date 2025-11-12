package com.github.frederikpietzko.cloudnativespring.events

import java.time.LocalDate
import java.util.UUID

data class CustomerRegisteredEvent(
    val customerId: UUID,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val email: String,
    val street: String,
    val houseNumber: String,
    val city: String,
    val postalCode: String,
    val country: String,
)

data class CustomerDeletedEvent(
    val customerId: UUID,
)