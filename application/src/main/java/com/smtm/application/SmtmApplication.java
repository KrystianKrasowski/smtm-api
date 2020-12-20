package com.smtm.application;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SmtmApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmtmApplication.class);
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
