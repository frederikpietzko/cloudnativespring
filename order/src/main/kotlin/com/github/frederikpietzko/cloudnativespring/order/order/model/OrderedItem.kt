package com.github.frederikpietzko.cloudnativespring.order.order.model

import com.github.frederikpietzko.cloudnativespring.infrastructure.BaseEntity
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import java.util.*

@Entity
class OrderedItem(
    val menuItemId: UUID,
    @Embedded val price: Price
) : BaseEntity()