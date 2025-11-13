package com.github.frederikpietzko.cloudnativespring.restaurant.restaurant

import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.repository.RestaurantRepository
import io.micrometer.core.instrument.binder.MeterBinder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BusinessMetricConfiguration {
    @Bean
    fun businessMetrics(restaurantRepository: RestaurantRepository): MeterBinder {
        return MeterBinder { registry ->
            registry.gauge("restaurant_count", restaurantRepository) { repo ->
                repo.count().toDouble()
            }
        }
    }
}




