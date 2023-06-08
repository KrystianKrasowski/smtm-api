package com.smtm.application.spring.resources

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SandboxResource(
    @Value("\${spring.datasource.url}") private val dbUrl: String
) {

    @GetMapping("/datasource")
    fun getDatasourceInfo(): String {
        return "Database url: $dbUrl"
    }
}