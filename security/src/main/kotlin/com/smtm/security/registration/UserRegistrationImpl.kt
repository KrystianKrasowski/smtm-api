package com.smtm.security.registration

import com.smtm.security.api.UserRegistration
import com.smtm.security.spi.UsersRepository

class UserRegistrationImpl(
    private val newUserValidator: NewUserValidator,
    private val usersRepository: UsersRepository
) : UserRegistration {

    override fun register(email: EmailAddress, password: UnencryptedPassword): UserProfile = newUserValidator.findViolations(email, password)
        .takeUnless { it.isEmpty() }
        ?.let { invalidUserProfileOf(it) }
        ?: usersRepository.register(email, password)
}
