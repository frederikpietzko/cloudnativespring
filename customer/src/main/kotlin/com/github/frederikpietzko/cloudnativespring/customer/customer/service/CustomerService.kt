package com.github.frederikpietzko.cloudnativespring.customer.customer.service

import com.github.frederikpietzko.cloudnativespring.customer.customer.model.Customer
import com.github.frederikpietzko.cloudnativespring.customer.customer.repository.CustomerRepository
import com.github.frederikpietzko.cloudnativespring.events.CustomerDeletedEvent
import com.github.frederikpietzko.cloudnativespring.events.CustomerRegisteredEvent
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val kafkaTemplate: KafkaTemplate<String, Any>,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(CustomerService::class.java)
    }

    fun getCustomer(customerId: UUID) =
        requireNotNull(customerRepository.findByIdOrNull(customerId)) {
            "Customer with id $customerId not found"
        }

    fun registerCustomer(customer: Customer): Customer {
        logger.info("Registering customer with id ${customer.id}")
        kafkaTemplate.send("customer-registered", customer.toRegisterEvent())
        return customer
    }

    @KafkaListener(topics = ["customer-registered"])
    @Transactional
    fun handleCustomerRegisteredEvent(event: CustomerRegisteredEvent) {
        logger.info("Handling CustomerRegisteredEvent for customer with id ${event.customerId}")
        customerRepository.save(event.toCustomer())
    }

    fun deleteCustomer(customerId: UUID) {
        val customer = getCustomer(customerId)
        logger.info("Deleting customer with id ${customer.id}")
        kafkaTemplate.send("customer-deleted", customer.toDeleteEvent())
    }

    @KafkaListener(topics = ["customer-deleted"])
    @Transactional
    fun handleCustomerDeletedEvent(event: CustomerDeletedEvent) {
        logger.info("Handling CustomerDeletedEvent for customer with id ${event.customerId}")
        customerRepository.deleteById(event.customerId)
    }

    private fun Customer.toRegisterEvent() = CustomerRegisteredEvent(
        customerId = id,
        firstName = personalData.firstName,
        lastName = personalData.lastName,
        dateOfBirth = personalData.dateOfBirth,
        email = personalData.emailAddress,
        street = address.street,
        houseNumber = address.houseNumber,
        city = address.city,
        postalCode = address.postalCode,
        country = address.country,
    )

    private fun CustomerRegisteredEvent.toCustomer() = Customer(
        personalData = com.github.frederikpietzko.cloudnativespring.customer.customer.model.PersonalData(
            firstName = firstName,
            lastName = lastName,
            dateOfBirth = dateOfBirth,
            emailAddress = email,
        ),
        address = com.github.frederikpietzko.cloudnativespring.customer.customer.model.Address(
            street = street,
            houseNumber = houseNumber,
            city = city,
            postalCode = postalCode,
            country = country,
        )
    )

    private fun Customer.toDeleteEvent() = CustomerDeletedEvent(
        customerId = id,
    )
}