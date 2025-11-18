package com.github.frederikpietzko.cloudnativespring.order.restaurant

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import java.util.*

@HttpExchange($$"${cloudnativespring.restaurant.service.url}")
interface RestaurantClient {
    @GetExchange("/menu-itmes/{restaurantId}", version = "1.0")
    fun getMenuItems(@PathVariable restaurantId: UUID): List<MenuItemDto>
}



