package com.github.frederikpietzko.cloudnativespring.events

import java.util.*

data class OrderPlacedEvent(
    val orderId: UUID,
    val customerId: UUID,
    val restaurantId: UUID,
    val items: List<OrderedItemEventDto>,
)

data class OrderedItemEventDto(
    val menuItemId: UUID,
    val price: PriceEventDto,
)