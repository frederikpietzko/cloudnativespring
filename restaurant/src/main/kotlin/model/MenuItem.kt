package com.github.frederikpietzko.cloudnativespring.restaurant.model

import com.github.frederikpietzko.cloudnativespring.infrastructure.BaseEntity
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne

@Entity
class MenuItem(
    val name: String,
    @Embedded val price: Price,
    @ManyToOne val restaurant: Restaurant,
    val category: String,
) : BaseEntity()