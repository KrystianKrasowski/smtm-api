package com.smtm.security.registration

import com.smtm.common.ConstraintViolation
import com.smtm.common.constraintViolationOf
import com.smtm.common.messageOf
import com.smtm.security.password.policy.basicPasswordValidatorOf
import com.smtm.security.spi.UsersRepository

private const val EmailPattern = "^[A-Za-zęóąśłżźćńĘÓĄŚŁŻŹĆŃ0-9._%+-]+@[A-ZęóąśłżźćńĘÓĄŚŁŻŹĆŃa-z0-9.-]+\\.[A-ZęóąśłżźćńĘÓĄŚŁŻŹĆŃa-z]{2,6}$"

class NewUserValidator(private val usersRepository: UsersRepository) {

    private val passwordValidator = basicPasswordValidatorOf()

    fun findViolations(email: EmailAddress, password: UnencryptedPassword): List<ConstraintViolation> = listOfNotNull(email.nonUnique(), email.invalid()) + password.unsecure()

    private fun EmailAddress.nonUnique() = constraintViolationOf("email", messageOf("Email %email% already exists", mapOf("email" to address)))
        .takeIf { usersRepository.isRegistered(this) }

    private fun EmailAddress.invalid() = constraintViolationOf("email", messageOf("%email% is not a valid email address", mapOf("email" to address)))
        .takeUnless { EmailPattern.toRegex().matches(address) }

    private fun UnencryptedPassword.unsecure() = getViolations(passwordValidator)
}
