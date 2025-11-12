package com.github.frederikpietzko.cloudnativespring.customer.customer.model

import com.github.frederikpietzko.cloudnativespring.infrastructure.BaseEntity
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import java.time.LocalDate
import java.time.OffsetDateTime

@Entity
class Customer(
    @Embedded val personalData: PersonalData,
    @Embedded val address: Address,
) : BaseEntity()

@Embeddable
class PersonalData(
    @Length(min=3) val firstName: String,
    @Length(min=3) val lastName: String,
    val dateOfBirth: LocalDate,
    @Email val emailAddress: String,
) {
}


@Embeddable
class Address(
    @Length(min=3) val street: String,
    @NotBlank val houseNumber: String,
    @Length(min=3) val city: String,
    @NotBlank val postalCode: String,
    @NotBlank val country: String,
)