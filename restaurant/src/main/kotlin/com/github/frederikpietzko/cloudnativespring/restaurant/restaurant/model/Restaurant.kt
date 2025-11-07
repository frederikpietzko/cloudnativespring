package com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.model

import com.github.frederikpietzko.cloudnativespring.infrastructure.BaseEntity
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany

@Entity
class Restaurant(
    var name: String,
    @Embedded var address: Address,
    @OneToMany(cascade = [ALL], orphanRemoval = true) val menuItems: MutableSet<MenuItem> = mutableSetOf(),
) : BaseEntity() {
    fun addMenuItem(menuItem: MenuItem) {
        menuItem.restaurant = this
        menuItems.add(menuItem)
    }
}

