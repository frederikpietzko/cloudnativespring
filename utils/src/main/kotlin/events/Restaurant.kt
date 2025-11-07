package com.github.frederikpietzko.cloudnativespring.events

import java.math.BigDecimal
import java.util.*

data class RestaurantRegisteredEvent(
    val restaurantId: UUID,
    val name: String,
    val address: RestaurantAddress,
)

data class RestaurantAddress(
    val street: String,
    val city: String,
    val postalCode: String,
    val country: String,
)

data class RestaurantAddressChangedEvent(
    val restaurantId: UUID,
    val address: RestaurantAddress,
)

data class MenuItemAddedEvent(
    val restaurantId: UUID,
    val menuItem: MenuItemEventDto,
)

data class MenuItemEventDto(
    val name: String,
    val price: PriceEventDto,
    val category: String,
)

data class PriceEventDto(
    val amount: BigDecimal,
    val currency: CurrencyEventDto,
)

enum class CurrencyEventDto {
    EUR
}