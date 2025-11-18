package com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.service

import com.github.frederikpietzko.cloudnativespring.events.RestaurantAddress
import com.github.frederikpietzko.cloudnativespring.events.RestaurantAddressChangedEvent
import com.github.frederikpietzko.cloudnativespring.events.RestaurantRegisteredEvent
import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.EventPublisher
import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.model.Address
import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.model.Restaurant
import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.repository.RestaurantRepository
import io.micrometer.tracing.annotation.NewSpan
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class RestaurantService(
    private val restaurantRepository: RestaurantRepository,
    private val eventPublisher: EventPublisher,
) {

    companion object {
        private val logger = LoggerFactory.getLogger(RestaurantService::class.java)
    }

    @Transactional
    @NewSpan
    fun registerRestaurant(restaurant: Restaurant): Restaurant {
        val restaurant = restaurantRepository.save(restaurant)
        logger.info("Registered restaurant with id ${restaurant.id}")
        eventPublisher.publishEvent(
            topic = "restaurant-registered",
            event = RestaurantRegisteredEvent(
                restaurantId = restaurant.id,
                name = restaurant.name,
                address = restaurant.address.into()
            )
        )
        return restaurant
    }

    @Transactional
    fun restaurantAddressChanged(restaurantId: UUID, address: Address): Restaurant {
        val restaurant = restaurantRepository.findByIdOrNull(restaurantId)
        requireNotNull(restaurant) { "Restaurant with id $restaurantId not found" }
        restaurant.address = address
        eventPublisher.publishEvent(
            topic = "restaurant-address-changed",
            event = RestaurantAddressChangedEvent(
                restaurantId = restaurant.id,
                address = address.into()
            )
        )

        return restaurant
    }


    private fun Address.into() = RestaurantAddress(
        street = street,
        city = city,
        postalCode = postalCode,
        country = country
    )
}