package com.smtm.security.password.policy

import com.smtm.common.ConstraintViolation
import com.smtm.common.constraintViolationOf
import com.smtm.common.messageOf
import com.smtm.security.registration.UnencryptedPassword

internal const val PasswordSpecialCharacters = "!@#$%^&*()_+=-"
internal const val PasswordMinLength = 8

class BasicPasswordPolicy(private val specialCharacters: String, private val minimumLength: Int) : PasswordPolicy {

    private val atLeastNumberOfSpecialChars = constraintViolationOf(
        property = "password",
        message = messageOf(
            pattern = "Password should contain at least %num% special character(s)",
            parameters = mapOf("num" to "1")
        )
    )

    private val atLeastNumberOfUppercaseChars = constraintViolationOf(
        property = "password",
        message = messageOf(
            pattern = "Password should contain at least %num% uppercase character(s)",
            parameters = mapOf("num" to "1")
        )
    )

    private val atLeastNumberOfDigits = constraintViolationOf(
        property = "password",
        message = messageOf(
            pattern = "Password should contain at least %num% digit",
            parameters = mapOf("num" to "1")
        )
    )

    private val atLeastNumberOfCharactersLong = constraintViolationOf(
        property = "password",
        message = messageOf(
            pattern = "Password should be at least %num% characters long",
            parameters = mapOf("num" to minimumLength.toString())
        )
    )

    override fun validate(password: UnencryptedPassword): Collection<ConstraintViolation> {
        return listOfNotNull(
            hasNoSpecialCharacter(password.toString()),
            hasNoUppercaseLetter(password.toString()),
            hasNoDigit(password.toString()),
            isTooShort(password.toString())
        )
    }

    private fun hasNoSpecialCharacter(value: String) = atLeastNumberOfSpecialChars
        .takeUnless { "[$specialCharacters]+".toRegex().containsMatchIn(value) }

    private fun hasNoUppercaseLetter(value: String) = atLeastNumberOfUppercaseChars
        .takeUnless { "[A-Z]+".toRegex().containsMatchIn(value) }

    private fun hasNoDigit(value: String) = atLeastNumberOfDigits
        .takeUnless { "[0-9]+".toRegex().containsMatchIn(value) }

    private fun isTooShort(value: String) = atLeastNumberOfCharactersLong
        .takeUnless { value.length >= minimumLength }
}

fun basicPasswordValidatorOf(specialCharacters: String = PasswordSpecialCharacters, minimumLength: Int = PasswordMinLength): PasswordPolicy {
    return BasicPasswordPolicy(specialCharacters, minimumLength)
}
