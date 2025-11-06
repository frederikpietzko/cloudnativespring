package com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.repository

import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.model.Restaurant
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RestaurantRepository : JpaRepository<Restaurant, UUID> {
}