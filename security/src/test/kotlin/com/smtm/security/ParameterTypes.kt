package com.smtm.security

import com.smtm.security.registration.*
import io.cucumber.java.DataTableType
import io.cucumber.java.ParameterType

class ParameterTypes {

    @ParameterType(".*")
    fun unencryptedPassword(input: String) = unencryptedPasswordOf(input)

    @ParameterType(".*")
    fun password(input: String) = passwordOf(input)

    @ParameterType("(not enough special characters|not enough uppercase letters|not enough digits|not enough length)")
    fun passwordViolation(input: String) = ViolationsMap.valueOf(input).violation

    @ParameterType(".*")
    fun emailAddress(input: String) = emailAddressOf(input)

    @DataTableType
    fun validUserProfileEntry(entry: Map<String, String>): ValidUserProfileEntry {
        return ValidUserProfileEntry(
                id = entry.getValue("id").toLong(),
                emailAddress = entry.getValue("email").toEmailAddress(),
                password = entry.getValue("password").toUnencryptedPassword()
        )
    }
}

data class ValidUserProfileEntry(val id: Long, val emailAddress: EmailAddress, val password: UnencryptedPassword)

private enum class ViolationsMap(val violation: Violation) {
    `not enough special characters`(Violation.NotEnoughSpecialChars),
    `not enough uppercase letters`(Violation.NotEnoughUppercaseLetters),
    `not enough digits`(Violation.NotEnoughDigits),
    `not enough length`(Violation.NotEnoughLength)
}
