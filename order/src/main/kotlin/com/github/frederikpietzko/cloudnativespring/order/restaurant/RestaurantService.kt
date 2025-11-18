package com.github.frederikpietzko.cloudnativespring.order.restaurant

import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class RestaurantService(
    circuitBreakerFactory: CircuitBreakerFactory<*, *>,
    private val restaurantClient: RestaurantClient,
) {
    private val retaurantCircuitBreaker = circuitBreakerFactory.create("restaurantService")


    fun getMenu(restaurantId: UUID): List<MenuItemDto> = retaurantCircuitBreaker.run {
        restaurantClient.getMenuItems(restaurantId)
    }

    fun getMenuByMenuItemId(restaurantId: UUID) =
        getMenu(restaurantId).associateBy { it.id }
}