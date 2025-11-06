package com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.service

import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.repository.RestaurantRepository
import org.springframework.stereotype.Service

@Service
class RestaurantService(
    private val restaurantRepository: RestaurantRepository,
) {
}