package com.smtm.application.security

import com.smtm.infrastructure.persistence.users.DbUsersRepository
import com.smtm.infrastructure.persistence.users.DbUsersRepositoryAdapter
import com.smtm.security.api.Authentication
import com.smtm.security.api.Authorization
import com.smtm.security.api.UserRegistration
import com.smtm.security.authentication.authenticationOf
import com.smtm.security.authorization.authorizationOf
import com.smtm.security.registration.userRegistrationOf
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
    fun userRegistration(usersRepository: UsersRepository): UserRegistration {
        return userRegistrationOf(usersRepository)
    }

    @Bean
    fun authentication(usersRepository: UsersRepository,
                       @Value("\${smtm.security.jwt.secret}") secret: String,
                       @Value("\${smtm.security.jwt.validity}") validityTime: Int,
                       clock: Clock): Authentication {
        return authenticationOf(usersRepository, secret, validityTime, clock)
    }

    @Bean
    fun authorization(@Value("\${smtm.security.jwt.secret}") secret: String): Authorization {
        return authorizationOf(secret)
    }
}
