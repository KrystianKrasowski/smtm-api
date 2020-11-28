package com.smtm.users.registration

import com.smtm.users.api.UserRegistration
import com.smtm.users.spi.PasswordEncryption
import com.smtm.users.spi.UsersRepository

internal class UserRegistrationImpl(
        private val usersRepository: UsersRepository,
        private val passwordEncryption: PasswordEncryption
) : UserRegistration {

    override fun register(email: String, password: UnsecuredPassword): UserProfile = findViolationsIn(email, password)
            .takeUnless { it.isEmpty() }
            ?.let { invalidUserProfileOf(it) }
            ?: usersRepository.register(email, password.encrypt(passwordEncryption))

    private fun findViolationsIn(email: String, password: UnsecuredPassword) = listOfNotNull(
            emailIsUnique(email),
            passwordMeetsPolicy(password)
    )

    private fun emailIsUnique(email: String) = constraintViolationOf("email", Violation.NonUnique)
            .takeIf { usersRepository.isRegistered(email) }

    private fun passwordMeetsPolicy(password: UnsecuredPassword) = constraintViolationOf("password", Violation.TooWeak)
            .takeUnless { password.isSecure() }
}

fun userRegistrationOf(usersRepository: UsersRepository, passwordEncryption: PasswordEncryption): UserRegistration = UserRegistrationImpl(usersRepository, passwordEncryption)
