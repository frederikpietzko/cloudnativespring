package com.github.frederikpietzko.cloudnativespring.gateway

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@Configuration
class Application {
}

fun main() {
    SpringApplication.run(Application::class.java)
}