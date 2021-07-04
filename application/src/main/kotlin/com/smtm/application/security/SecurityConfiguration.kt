package com.smtm.application.security

import com.smtm.infrastructure.persistence.users.DbUsersRepository
import com.smtm.infrastructure.persistence.users.DbUsersRepositoryAdapter
import com.smtm.security.api.CredentialsAuthentication
import com.smtm.security.api.Authorization
import com.smtm.security.api.UserRegistration
import com.smtm.security.authentication.AuthenticationImpl
import com.smtm.security.authentication.TokenFactory
import com.smtm.security.authorization.AuthorizationImpl
import com.smtm.security.registration.NewUserValidator
import com.smtm.security.registration.UserRegistrationImpl
import com.smtm.security.spi.AuthenticationSettings
import com.smtm.security.spi.UsersRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Clock

@Configuration
class SecurityConfiguration {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun usersRepository(dbUsersRepository: DbUsersRepository, passwordEncoder: PasswordEncoder): UsersRepository {
        return DbUsersRepositoryAdapter(dbUsersRepository, passwordEncoder)
    }

    @Bean
    fun authenticationSettings(@Value("\${smtm.security.jwt.secret}") secret: String,
                               @Value("\${smtm.security.jwt.access-token-validity}") accessTokenValidity: Int,
                               @Value("\${smtm.security.jwt.refresh-token-validity}") refreshTokenValidity: Int): AuthenticationSettings {
        return AuthenticationSettingsAdapter(secret, accessTokenValidity, refreshTokenValidity)
    }

    @Bean
    fun userRegistration(usersRepository: UsersRepository): UserRegistration {
        return UserRegistrationImpl(
            newUserValidator = NewUserValidator(
                usersRepository = usersRepository
            ),
            usersRepository = usersRepository
        )
    }

    @Bean
    fun authentication(usersRepository: UsersRepository,
                       authenticationSettings: AuthenticationSettings,
                       clock: Clock): CredentialsAuthentication {
        return AuthenticationImpl(
            usersRepository = usersRepository,
            tokenFactory = TokenFactory(
                authenticationSettings = authenticationSettings,
                clock = clock
            )
        )
    }

    @Bean
    fun authorization(authenticationSettings: AuthenticationSettings, clock: Clock): Authorization {
        return AuthorizationImpl(
            tokenFactory = TokenFactory(
                authenticationSettings = authenticationSettings,
                clock = clock
            )
        )
    }
}
