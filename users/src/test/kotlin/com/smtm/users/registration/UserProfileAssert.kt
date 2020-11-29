package com.smtm.users.registration

import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat

class UserProfileAssert(userProfile: UserProfile?) : AbstractAssert<UserProfileAssert, UserProfile>(userProfile, UserProfileAssert::class.java) {

    private val actualAsValid
        get() = actual as UserProfile.Valid

    private val actualAsInvalid
        get() = actual as UserProfile.Invalid

    fun hasEmail(email: String): UserProfileAssert {
        isValid()
        assertThat(actualAsValid.email).isEqualTo(email)
        return myself
    }

    fun hasConstraintViolation(key: String, violation: Violation) {
        isInvalid()
        assertThat(actualAsInvalid.violations).contains(constraintViolationOf(key, violation))
    }

    fun hasAnyConstraintViolationFor(key: String) {
        isInvalid()
        assertThat(actualAsInvalid.violations.find { it.key == key }).isNotNull
    }

    private fun isValid(): UserProfileAssert {
        isNotNull
        isInstanceOf(UserProfile.Valid::class.java)
        return myself
    }

    private fun isInvalid(): UserProfileAssert {
        isNotNull
        isInstanceOf(UserProfile.Invalid::class.java)
        return myself
    }
}
