package com.smtm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SmtmApplication

fun main(args: Array<String>) {
    runApplication<SmtmApplication>(*args)
}
