package com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.service

import com.github.frederikpietzko.cloudnativespring.events.CurrencyEventDto
import com.github.frederikpietzko.cloudnativespring.events.MenuItemAddedEvent
import com.github.frederikpietzko.cloudnativespring.events.MenuItemEventDto
import com.github.frederikpietzko.cloudnativespring.events.PriceEventDto
import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.EventPublisher
import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.model.MenuItem
import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.model.Price
import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.repository.RestaurantRepository
import io.opentelemetry.instrumentation.annotations.WithSpan
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class MenuService(
    private val restaurantRepository: RestaurantRepository,
    private val eventPublisher: EventPublisher,
) {

    @Transactional
    fun getMenuItems(restaurantId: UUID): List<MenuItem> {
        val restaurant = restaurantRepository.findByIdOrNull(restaurantId)
        requireNotNull(restaurant) { "Restaurant with id $restaurantId not found" }
        return restaurant.menuItems.toList()
    }

    @Transactional
    @WithSpan
    fun addMenuItem(restaurantId: UUID, menuItem: MenuItem): MenuItem {
        val restaurant = restaurantRepository.findByIdOrNull(restaurantId)
        requireNotNull(restaurant) { "Restaurant with id $restaurantId not found" }
        restaurant.addMenuItem(menuItem)
        eventPublisher.publishEvent(
            "menu-item-added", MenuItemAddedEvent(
                restaurantId = restaurant.id,
                menuItem = menuItem.into()
            )
        )
        return menuItem
    }

    private fun MenuItem.into() = MenuItemEventDto(
        name = name,
        price = price.into(),
        category = category
    )

    private fun Price.into() = PriceEventDto(
        amount = amount,
        currency = CurrencyEventDto.EUR
    )
}