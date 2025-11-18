package com.github.frederikpietzko.cloudnativespring.order.order.model

import com.github.frederikpietzko.cloudnativespring.infrastructure.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import java.util.*

@Entity
class CustomerOrder(
    val restaurantId: UUID,
    val customerId: UUID,
    @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true, fetch = FetchType.EAGER)
    val orderedItems: Set<OrderedItem>,
) : BaseEntity() {
    val totalPrice get() = orderedItems.sumOf { it.price.amount }
}
