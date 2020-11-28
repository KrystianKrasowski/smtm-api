package com.smtm.users.registration

import org.assertj.core.api.AbstractAssert

class UserProfileAssert(userProfile: UserProfile?) : AbstractAssert<UserProfileAssert, UserProfile>(userProfile, UserProfileAssert::class.java) {

    private val actualAsValid
        get() = actual as UserProfile.Valid

    fun hasId(id: Long): UserProfileAssert {
        isValid()

        if (actualAsValid.id != id) {
            failWithMessage("Expected id to be <%s>, but was <%s>", id, actualAsValid.id)
        }

        return myself
    }

    fun hasEmail(email: String): UserProfileAssert {
        isValid()

        if (actualAsValid.email != email) {
            failWithMessage("Expected email to be <%s>, but was <%s>", email, actualAsValid.email)
        }

        return myself
    }

    fun isValid(): UserProfileAssert {
        isNotNull
        isInstanceOf(UserProfile.Valid::class.java)
        return myself
    }
}
