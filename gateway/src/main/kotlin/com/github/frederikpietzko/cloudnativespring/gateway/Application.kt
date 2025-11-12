package com.github.frederikpietzko.cloudnativespring.gateway

import io.opentelemetry.instrumentation.spring.autoconfigure.OpenTelemetryAutoConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@Configuration
@ConfigurationPropertiesScan
class Application {

    @Bean
    fun routeLocator(
        builder: RouteLocatorBuilder,
        servicesConfiguration: ServicesConfiguration
    ): RouteLocator {
        return builder.routes {
            route("restaurant-service") {
                path("/restaurants/**")
                uri(servicesConfiguration.restaurantServiceUrl)
            }
            route("order-service") {
                path("/orders/**")
                uri(servicesConfiguration.orderServiceUrl)
            }
            route("customer-service") {
                path("/customers", "/customers/**")
                uri(servicesConfiguration.customerServiceUrl)
            }
        }
    }
}

@ConfigurationProperties(prefix = "gateway.services")
data class ServicesConfiguration(
    val restaurantServiceUrl: String,
    val orderServiceUrl: String,
    val customerServiceUrl: String,
)

fun main() {
    SpringApplication.run(Application::class.java)
}