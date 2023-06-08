package com.smtm.application.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SmtmSpringApplication

fun main(args: Array<String>) {
    runApplication<SmtmSpringApplication>(*args)
}
