package com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.model

import com.github.frederikpietzko.cloudnativespring.infrastructure.BaseEntity
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne

@Entity
class MenuItem(
    val name: String,
    @Embedded val price: Price,
    var category: String,
) : BaseEntity() {
    @ManyToOne
    lateinit var restaurant: Restaurant
}