package com.github.frederikpietzko.cloudnativespring.restaurant.model

import jakarta.persistence.Embeddable

@Embeddable
class Address(
    val street: String,
    val city: String,
    val postalCode: String,
    val country: String,
)