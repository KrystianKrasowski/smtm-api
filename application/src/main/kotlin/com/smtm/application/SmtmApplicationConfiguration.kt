package com.smtm.application

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class SmtmApplicationConfiguration {

    @Bean
    fun clock(): Clock {
        return Clock.systemUTC()
    }
}
