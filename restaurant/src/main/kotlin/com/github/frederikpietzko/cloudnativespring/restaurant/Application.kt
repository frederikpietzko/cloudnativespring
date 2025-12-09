package com.github.frederikpietzko.cloudnativespring.restaurant

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(scanBasePackages = ["com.github.frederikpietzko.cloudnativespring"])
@EnableScheduling
class Application

fun main() {
    SpringApplication.run(Application::class.java)
}