package com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.api

import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.model.Currency
import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.model.MenuItem
import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.model.Price
import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.service.MenuService
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Positive
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*

@RestController
@RequestMapping("/menu-items")
class MenuItemApi(
    private val menuService: MenuService,
) {

    @GetMapping("/{restaurantId}", version = "1.0")
    fun getMenuItems(@PathVariable restaurantId: UUID): List<MenuItemDto> {
        val menuItems = menuService.getMenuItems(restaurantId)
        return menuItems.map { it.into() }
    }

    @PostMapping(version = "1.0")
    fun addMenuItem(@Valid @RequestBody request: AddMenuItemRequestDto): MenuItemDto {
        val menuItem = menuService.addMenuItem(
            restaurantId = request.restaurantId,
            menuItem = request.into()
        )
        return menuItem.into()
    }

    private fun MenuItem.into() = MenuItemDto(
        id = id,
        name = name,
        price = price.amount,
        currency = when (price.currency) {
            Currency.EUR -> CurrencyDto.EUR
        },
        category = category,
    )
}

data class AddMenuItemRequestDto(
    val restaurantId: UUID,
    @Min(3) val name: String,
    @Positive val price: BigDecimal,
    val currency: CurrencyDto,
    @Min(3) val category: String,
) {
    fun into() = MenuItem(
        name = name,
        price = Price(
            amount = price,
            currency = when (currency) {
                CurrencyDto.EUR -> Currency.EUR
            }
        ),
        category = category,
    )
}

data class MenuItemDto(
    val id: UUID,
    val name: String,
    val price: BigDecimal,
    val currency: CurrencyDto,
    val category: String,
)

enum class CurrencyDto {
    EUR
}