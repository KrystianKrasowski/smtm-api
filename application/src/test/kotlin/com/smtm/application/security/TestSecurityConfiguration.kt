package com.smtm.application.security

import com.smtm.application.security.v1.TokensController
import com.smtm.application.security.v1.UsersController
import com.smtm.security.api.CredentialsAuthentication
import com.smtm.security.api.Authorization
import com.smtm.security.api.RefreshTokenAuthentication
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
    fun credentialsAuthentication(): CredentialsAuthentication = FakeCredentialsAuthentication()

    @Bean
    fun refreshTokenAuthentication(): RefreshTokenAuthentication = FakeRefreshTokenAuthentication()

    @Bean
    fun usersController(userRegistration: UserRegistration): UsersController = UsersController(userRegistration)

    @Bean
    fun tokenController(credentialsAuthentication: CredentialsAuthentication,
                        refreshTokenAuthentication: RefreshTokenAuthentication): TokensController = TokensController(credentialsAuthentication, refreshTokenAuthentication)

    @Bean
    fun testProtectedResourceController(): TestProtectedResourceController = TestProtectedResourceController()
}
