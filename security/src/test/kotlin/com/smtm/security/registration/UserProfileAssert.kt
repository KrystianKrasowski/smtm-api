package com.smtm.security.registration

import com.smtm.common.constraintViolationOf
import com.smtm.common.messageOf
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat

class UserProfileAssert(userProfile: UserProfile?) : AbstractAssert<UserProfileAssert, UserProfile>(userProfile, UserProfileAssert::class.java) {

    private val actualAsValid
        get() = actual as UserProfile.Valid

    private val actualAsInvalid
        get() = actual as UserProfile.Invalid

    fun hasEmail(email: EmailAddress): UserProfileAssert {
        isValid()
        assertThat(actualAsValid.email).isEqualTo(email)
        return myself
    }

    fun hasConstraintViolation(property: String, pattern: String, parameters: Map<String, String>) {
        isInvalid()
        assertThat(actualAsInvalid.violations).contains(constraintViolationOf(property, messageOf(pattern, parameters)))
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
