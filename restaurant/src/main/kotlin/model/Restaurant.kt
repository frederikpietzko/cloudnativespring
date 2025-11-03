package com.github.frederikpietzko.cloudnativespring.restaurant.model

import com.github.frederikpietzko.cloudnativespring.infrastructure.BaseEntity
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany

@Entity
class Restaurant(
    val name: String,
    @Embedded val address: Address,
    @OneToMany val menuItems: MutableSet<MenuItem> = mutableSetOf(),
) : BaseEntity()

