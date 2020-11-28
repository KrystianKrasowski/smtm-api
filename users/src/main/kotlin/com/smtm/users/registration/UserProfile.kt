package com.smtm.users.registration

sealed class UserProfile {

    data class Valid internal constructor(val id: Long, val email: String) : UserProfile()
    data class Invalid internal constructor(val violations: List<ConstraintViolation>) : UserProfile()
}

fun validUserProfileOf(id: Long, email: String) = UserProfile.Valid(id, email)

fun invalidUserProfileOf(violations: List<ConstraintViolation>) = UserProfile.Invalid(violations)
