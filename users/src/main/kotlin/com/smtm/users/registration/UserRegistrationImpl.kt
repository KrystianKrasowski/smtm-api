package com.smtm.users.registration

import com.smtm.users.api.UserRegistration
import com.smtm.users.spi.PasswordEncryption
import com.smtm.users.spi.UsersRepository

internal class UserRegistrationImpl(
        private val newUserValidator: NewUserValidator,
        private val usersRepository: UsersRepository,
        private val passwordEncryption: PasswordEncryption
) : UserRegistration {

    override fun register(email: EmailAddress, password: UnencryptedPassword): UserProfile = newUserValidator.findViolations(email, password)
            .takeUnless { it.isEmpty() }
            ?.let { invalidUserProfileOf(it) }
            ?: usersRepository.register(email, password.encrypt(passwordEncryption))
}

fun userRegistrationOf(usersRepository: UsersRepository, passwordEncryption: PasswordEncryption): UserRegistration {
    return UserRegistrationImpl(newUserValidatorOf(usersRepository), usersRepository, passwordEncryption)
}
