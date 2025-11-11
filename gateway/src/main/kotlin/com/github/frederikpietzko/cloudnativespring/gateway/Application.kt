package com.github.frederikpietzko.cloudnativespring.gateway

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@Configuration
class Application {

    @Bean
    fun routeLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes {
            route("restaurant-service") {
                path("/restaurants/**")
                uri("lb://restaurant-service")
            }
        }
    }
}

fun main() {
    SpringApplication.run(Application::class.java)
}