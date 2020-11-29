package com.smtm.users.password.policy

import com.smtm.users.registration.UnencryptedPassword
import com.smtm.users.registration.Violation

internal const val PasswordSpecialCharacters = "!@#$%^&*()_+=-"
internal const val PasswordMinLength = 8

class BasicPasswordPolicy(private val specialCharacters: String, private val minimumLength: Int) : PasswordPolicy {

    override fun validate(password: UnencryptedPassword): Collection<Violation> {
        return setOfNotNull(
                hasNoSpecialCharacter(password.toString()),
                hasNoUppercaseLetter(password.toString()),
                hasNoDigit(password.toString()),
                isTooShort(password.toString())
        )
    }

    private fun hasNoSpecialCharacter(value: String) = Violation.NotEnoughSpecialChars
            .takeUnless { "[$specialCharacters]+".toRegex().containsMatchIn(value) }

    private fun hasNoUppercaseLetter(value: String) = Violation.NotEnoughUppercaseLetters
            .takeUnless { "[A-Z]+".toRegex().containsMatchIn(value) }

    private fun hasNoDigit(value: String) = Violation.NotEnoughDigits
            .takeUnless { "[0-9]+".toRegex().containsMatchIn(value) }

    private fun isTooShort(value: String) = Violation.NotEnoughLength
            .takeUnless { value.length >= minimumLength }
}

fun basicPasswordValidatorOf(specialCharacters: String = PasswordSpecialCharacters, minimumLength: Int = PasswordMinLength): PasswordPolicy {
    return BasicPasswordPolicy(specialCharacters, minimumLength)
}
