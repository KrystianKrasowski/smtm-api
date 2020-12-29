package com.smtm.application

import org.junit.runner.RunWith
import io.cucumber.junit.Cucumber
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest
import com.smtm.application.security.TestSecurityConfiguration
import com.smtm.application.categories.TestCategoriesConfiguration
import com.smtm.application.security.WebSecurityConfiguration
import io.cucumber.junit.CucumberOptions
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@RunWith(Cucumber::class)
@CucumberOptions(features = ["doc/features"])
@CucumberContextConfiguration
@SpringBootTest(classes = [
    TestSecurityConfiguration::class,
    WebSecurityConfiguration::class,
    TestCategoriesConfiguration::class
])
@AutoConfigureMockMvc
@EnableWebMvc
class SmtmApplicationTest
