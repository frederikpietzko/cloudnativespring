package com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.api

import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.model.Address
import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.model.Restaurant
import com.github.frederikpietzko.cloudnativespring.restaurant.restaurant.service.RestaurantService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class RestaurantRegistrationApi(
    private val restaurantService: RestaurantService,
) {

    @PostMapping("/register", version = "1.0")
    fun registerRestaurant(@Valid @RequestBody request: RegisterRestaurantDto): RestaurantDto {
        val restaurant = restaurantService.registerRestaurant(
            Restaurant(
                name = request.name,
                address = Address(
                    street = request.street,
                    city = request.city,
                    postalCode = request.postalCode,
                    country = request.country,
                )
            )
        )
        return RestaurantDto(
            id = restaurant.id,
            name = restaurant.name,
        )
    }

    @PostMapping("/{id}/change-address", version = "1.0")
    fun changeRestaurantAddress(
        @Valid @RequestBody request: RestaurantAddressDto,
        @PathVariable id: UUID
    ) {
        restaurantService.restaurantAddressChanged(
            id,
            Address(
                street = request.street,
                city = request.city,
                postalCode = request.postalCode,
                country = request.country,
            ),
        )
    }
}

data class RegisterRestaurantDto(
    @NotBlank val name: String,
    @NotBlank val street: String,
    @NotBlank val city: String,
    @NotBlank val postalCode: String,
    @NotBlank val country: String,
)

data class RestaurantDto(
    val id: UUID,
    val name: String,
)

data class RestaurantAddressDto(
    @NotBlank val street: String,
    @NotBlank val city: String,
    @NotBlank val postalCode: String,
    @NotBlank val country: String,
)