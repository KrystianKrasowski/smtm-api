package com.smtm.users.registration

import com.smtm.users.password.policy.basicPasswordValidatorOf
import com.smtm.users.spi.UsersRepository

internal class NewUserValidator(private val usersRepository: UsersRepository) {

    private val passwordValidator = basicPasswordValidatorOf()

    fun findViolations(email: EmailAddress, password: UnencryptedPassword) = listOfNotNull(email.nonUnique()) + email.invalid() + password.unsecure()

    private fun EmailAddress.nonUnique() = constraintViolationOf("email", Violation.NonUnique)
            .takeIf { usersRepository.isRegistered(this) }

    private fun EmailAddress.invalid() = getViolations()
            .map { constraintViolationOf("email", it) }

    private fun UnencryptedPassword.unsecure() = getViolations(passwordValidator)
            .map { constraintViolationOf("password", it) }
}

internal fun newUserValidatorOf(usersRepository: UsersRepository) = NewUserValidator(usersRepository)
