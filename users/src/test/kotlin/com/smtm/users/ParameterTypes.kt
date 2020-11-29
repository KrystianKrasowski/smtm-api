package com.smtm.users.registration

import io.cucumber.java.ParameterType

class ParameterTypes {

    @ParameterType(".*")
    fun unsecuredPassword(input: String) = unencryptedPasswordOf(input)

    @ParameterType("(not enough special characters|not enough uppercase letters|not enough digits|not enough length)")
    fun passwordViolation(input: String) = ViolationsMap.valueOf(input).violation

    @ParameterType(".*")
    fun emailAddress(input: String) = emailAddressOf(input)
}

private enum class ViolationsMap(val violation: Violation) {
    `not enough special characters`(Violation.NotEnoughSpecialChars),
    `not enough uppercase letters`(Violation.NotEnoughUppercaseLetters),
    `not enough digits`(Violation.NotEnoughDigits),
    `not enough length`(Violation.NotEnoughLength)
}
