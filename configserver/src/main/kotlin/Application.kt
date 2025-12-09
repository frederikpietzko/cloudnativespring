package com.frederikpietzko.cloudnativespring.configserver

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.config.server.EnableConfigServer

@SpringBootApplication
@EnableConfigServer
class Application


fun main() {
    SpringApplication.run(Application::class.java)
}