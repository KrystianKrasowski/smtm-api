package com.smtm.application.security

import com.smtm.application.security.v1.TokenController
import com.smtm.application.security.v1.UsersController
import com.smtm.security.api.Authentication
import com.smtm.security.api.Authorization
import com.smtm.security.api.UserRegistration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestSecurityConfiguration {

    @Bean
    fun userRegistration(): UserRegistration = FakeUserRegistration()

    @Bean
    fun authorization(): Authorization = FakeAuthorization()

    @Bean
    fun authentication(): Authentication = FakeAuthentication()

    @Bean
    fun usersController(userRegistration: UserRegistration): UsersController = UsersController(userRegistration)

    @Bean
    fun tokenController(authentication: Authentication): TokenController = TokenController(authentication)

    @Bean
    fun testProtectedResourceController(): TestProtectedResourceController = TestProtectedResourceController()
}
