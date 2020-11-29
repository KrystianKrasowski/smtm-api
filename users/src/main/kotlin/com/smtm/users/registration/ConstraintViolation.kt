package com.smtm.users.registration

data class ConstraintViolation internal constructor(val key: String, val violation: Violation)

enum class Violation {
    NonUnique,
    TooWeak,
    NotEnoughSpecialChars,
    NotEnoughUppercaseLetters,
    NotEnoughDigits,
    NotEnoughLength,
    NotAnEmailAddress
}

fun constraintViolationOf(key: String, violation: Violation) = ConstraintViolation(key, violation)
