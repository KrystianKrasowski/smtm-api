package com.smtm.security.registration

data class ConstraintViolation internal constructor(val property: String, val violation: Violation)

enum class Violation {
    NonUnique,
    NotEnoughSpecialChars,
    NotEnoughUppercaseLetters,
    NotEnoughDigits,
    NotEnoughLength,
    NotAnEmailAddress
}

fun constraintViolationOf(key: String, violation: Violation) = ConstraintViolation(key, violation)
