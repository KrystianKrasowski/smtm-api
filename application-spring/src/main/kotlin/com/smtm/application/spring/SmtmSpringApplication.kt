package com.smtm.application.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories("com.smtm.infrastructure.persistence.*")
@EntityScan("com.smtm.infrastructure.persistence.*")
class SmtmSpringApplication

fun main(args: Array<String>) {
    runApplication<SmtmSpringApplication>(*args)
}
