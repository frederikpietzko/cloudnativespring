package com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.repository

import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.model.MenuItem
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MenuItemRepository : JpaRepository<MenuItem, UUID> {
}