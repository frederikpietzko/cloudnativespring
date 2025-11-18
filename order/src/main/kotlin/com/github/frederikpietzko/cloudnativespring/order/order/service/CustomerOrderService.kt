package com.github.frederikpietzko.cloudnativespring.order.order.service

import com.github.frederikpietzko.cloudnativespring.events.CurrencyEventDto
import com.github.frederikpietzko.cloudnativespring.events.OrderPlacedEvent
import com.github.frederikpietzko.cloudnativespring.events.OrderedItemEventDto
import com.github.frederikpietzko.cloudnativespring.events.PriceEventDto
import com.github.frederikpietzko.cloudnativespring.order.order.model.Currency
import com.github.frederikpietzko.cloudnativespring.order.order.model.CustomerOrder
import com.github.frederikpietzko.cloudnativespring.order.order.model.OrderedItem
import com.github.frederikpietzko.cloudnativespring.order.order.model.Price
import com.github.frederikpietzko.cloudnativespring.order.order.repository.OrderRepository
import com.github.frederikpietzko.cloudnativespring.order.restaurant.MenuItemDto
import com.github.frederikpietzko.cloudnativespring.order.restaurant.RestaurantService
import io.opentelemetry.instrumentation.annotations.WithSpan
import jakarta.transaction.Transactional
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerOrderService(
    private val orderRepository: OrderRepository,
    private val restaurantService: RestaurantService,
    private val kafkaTemplate: KafkaTemplate<String, Any>,
) {

    @WithSpan
    fun order(
        customerId: UUID,
        restaurantId: UUID,
        orderedItem: List<UUID>,
    ): CustomerOrder {
        val menu = restaurantService.getMenuByMenuItemId(restaurantId)
        val items = orderedItem
            .map { itemId ->
                requireNotNull(menu[itemId]) {
                    "Menu item with id $itemId not found in restaurant $restaurantId"
                }
            }
            .map { it.toOrderedItem() }
            .toSet()
        val customerOrder = CustomerOrder(
            customerId = customerId,
            restaurantId = restaurantId,
            orderedItems = items,
        )
        kafkaTemplate.send(
            "order-placed",
            customerOrder.toEvent()
        )
        return customerOrder
    }

    @KafkaListener(topics = ["order-placed"])
    @Transactional
    @WithSpan
    fun handleOrderPlacedEvent(event: OrderPlacedEvent) {
        val customerOrder = event.toCustomerOrder()
        orderRepository.save(customerOrder)
    }

    private fun MenuItemDto.toOrderedItem() = OrderedItem(
        menuItemId = id,
        price = Price(
            amount = price,
            currency = Currency.valueOf(currency.name)
        )
    )

    private fun CustomerOrder.toEvent() = OrderPlacedEvent(
        orderId = id,
        customerId = customerId,
        restaurantId = restaurantId,
        items = orderedItems.map {
            OrderedItemEventDto(
                menuItemId = it.menuItemId,
                price = PriceEventDto(
                    amount = it.price.amount,
                    currency = CurrencyEventDto.valueOf(it.price.currency.name)
                )
            )
        }
    )

    private fun OrderPlacedEvent.toCustomerOrder() = CustomerOrder(
        customerId = customerId,
        restaurantId = restaurantId,
        orderedItems = items.map {
            OrderedItem(
                menuItemId = it.menuItemId,
                price = Price(
                    amount = it.price.amount,
                    currency = Currency.valueOf(it.price.currency.name)
                )
            )
        }.toSet()
    ).apply { id = orderId }
}