package com.smtm.security.registration

import com.smtm.common.ConstraintViolation

sealed class UserProfile {

    data class Valid internal constructor(val id: Long, val email: EmailAddress) : UserProfile()
    data class Invalid internal constructor(val violations: List<ConstraintViolation>) : UserProfile()
}

fun validUserProfileOf(id: Long, email: EmailAddress) = UserProfile.Valid(id, email)

fun invalidUserProfileOf(violations: List<ConstraintViolation>) = UserProfile.Invalid(violations)
