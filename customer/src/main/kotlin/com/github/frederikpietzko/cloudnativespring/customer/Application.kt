package com.github.frederikpietzko.cloudnativespring.customer

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["com.github.frederikpietzko.cloudnativespring"])
class Application


fun main() {
    SpringApplication.run(Application::class.java)
}