package com.smtm.application.spring.security

import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfiguration(env: Environment) : WebMvcConfigurer {

    private val properties = ConfigurationProperties(
        allowedOrigins = env["CORS_ALLOWED_ORIGINS"]?.toArray() ?: emptyArray(),
        allowedMethods = env["CORS_ALLOWED_METHODS"]?.toArray() ?: emptyArray()
    )

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(*properties.allowedOrigins)
            .allowedMethods(*properties.allowedMethods)
    }

    private fun String.toArray() = split(',')
        .map { it.trim() }
        .toTypedArray()
}

private class ConfigurationProperties(val allowedOrigins: Array<String>, val allowedMethods: Array<String>)