package com.smtm.users.registration

import com.smtm.users.api.UserRegistration
import com.smtm.users.spi.PasswordEncryption
import com.smtm.users.spi.UsersRepository

internal class UserRegistrationImpl(
        private val usersRepository: UsersRepository,
        private val passwordEncryption: PasswordEncryption
) : UserRegistration {

    override fun register(email: String, password: UnsecuredPassword): UserProfile {
        if (usersRepository.hasNoEmailAs(email)) {
            return usersRepository.register(email, password.encrypt(passwordEncryption))
        } else {
            return invalidUserProfileOf(mapOf(
                    "email" to Violation.NonUnique
            ))
        }
    }
}

fun userRegistrationOf(usersRepository: UsersRepository, passwordEncryption: PasswordEncryption): UserRegistration = UserRegistrationImpl(usersRepository, passwordEncryption)
