package com.github.frederikpietzko.cloudnativespring.order.order.repository

import com.github.frederikpietzko.cloudnativespring.order.order.model.CustomerOrder
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderRepository : JpaRepository<CustomerOrder, UUID>