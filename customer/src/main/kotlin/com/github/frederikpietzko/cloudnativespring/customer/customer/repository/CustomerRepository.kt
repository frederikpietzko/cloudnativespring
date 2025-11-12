package com.github.frederikpietzko.cloudnativespring.customer.customer.repository

import com.github.frederikpietzko.cloudnativespring.customer.customer.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerRepository : JpaRepository<Customer, UUID>