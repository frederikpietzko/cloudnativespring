package com.github.frederikpietzko.cloudnativespring.customer.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration : WebMvcConfigurer {

    override fun configureApiVersioning(configurer: ApiVersionConfigurer) {
        configurer.useRequestHeader("API-Version")
    }
}