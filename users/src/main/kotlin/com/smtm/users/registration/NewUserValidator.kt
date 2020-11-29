package com.smtm.users.registration

import com.smtm.users.password.policy.basicPasswordValidatorOf
import com.smtm.users.spi.UsersRepository

internal class NewUserValidator(private val usersRepository: UsersRepository) {

    private val passwordValidator = basicPasswordValidatorOf()

    fun findViolations(email: String, password: UnencryptedPassword) = listOfNotNull(email.nonUnique()) + password.unsecure()

    private fun String.nonUnique() = constraintViolationOf("email", Violation.NonUnique)
            .takeIf { usersRepository.isRegistered(this) }

    private fun UnencryptedPassword.unsecure() = getViolations(passwordValidator)
            .map { constraintViolationOf("password", it) }
}

internal fun newUserValidatorOf(usersRepository: UsersRepository) = NewUserValidator(usersRepository)
