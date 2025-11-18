package com.github.frederikpietzko.cloudnativespring.order.order.model

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.math.BigDecimal

@Embeddable
class Price(
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING) val currency: Currency,
)