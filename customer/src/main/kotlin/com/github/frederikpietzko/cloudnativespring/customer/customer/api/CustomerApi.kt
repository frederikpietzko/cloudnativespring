package com.github.frederikpietzko.cloudnativespring.customer.customer.api

import com.github.frederikpietzko.cloudnativespring.customer.customer.model.Address
import com.github.frederikpietzko.cloudnativespring.customer.customer.model.Customer
import com.github.frederikpietzko.cloudnativespring.customer.customer.model.PersonalData
import com.github.frederikpietzko.cloudnativespring.customer.customer.service.CustomerService
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@RestController
class CustomerApi(
    private val customerService: CustomerService,
) {

    @GetMapping("/{customerId}", version = "1.0")
    fun getCustomer(@PathVariable customerId: UUID) =
        customerService.getCustomer(customerId).toDto()

    @PostMapping("/register", version = "1.0")
    fun createCustomer(@RequestBody @Valid request: RegisterCustomerRequestDto): CustomerDto {
        return customerService
            .registerCustomer(request.toCustomer())
            .toDto()
    }

    @DeleteMapping("/{customerId}", version = "1.0")
    fun deleteCustomer(@PathVariable customerId: UUID) {
        customerService.deleteCustomer(customerId)
    }

    private fun Customer.toDto() =
        CustomerDto(
            id = id,
            firstName = personalData.firstName,
            lastName = personalData.lastName,
            email = personalData.emailAddress,
            street = address.street,
            houseNumber = address.houseNumber,
            city = address.city,
            postalCode = address.postalCode,
            country = address.country,
        )

}

data class CustomerDto(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val street: String,
    val houseNumber: String,
    val city: String,
    val postalCode: String,
    val country: String,
)

data class RegisterCustomerRequestDto(
    @Length(min = 3) val firstName: String,
    @Length(min = 3) val lastName: String,
    @Email val email: String,
    @Length(min = 3) val street: String,
    @NotBlank val houseNumber: String,
    @Length(min = 3) val city: String,
    @NotBlank val postalCode: String,
    @NotBlank val country: String,
    val dateOfBirth: LocalDate,
) {
    fun toCustomer() = Customer(
        personalData = PersonalData(
            firstName = firstName,
            lastName = lastName,
            dateOfBirth = dateOfBirth,
            emailAddress = email,
        ),
        address = Address(
            street = street,
            houseNumber = houseNumber,
            city = city,
            postalCode = postalCode,
            country = country,
        )
    )
}