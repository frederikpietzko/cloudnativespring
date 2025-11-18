package com.github.frederikpietzko.cloudnativespring.order

import com.github.frederikpietzko.cloudnativespring.order.restaurant.RestaurantClient
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.service.registry.ImportHttpServices

@SpringBootApplication
@ImportHttpServices(RestaurantClient::class)
class Application

fun main() {
    SpringApplication.run(Application::class.java)
}
