package com.github.frederikpietzko.cloudnativespring.order.restaurant

import java.math.BigDecimal
import java.util.*

data class MenuItemDto(
    val id: UUID,
    val name: String,
    val price: BigDecimal,
    val currency: CurrencyDto,
    val category: String,
)