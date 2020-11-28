package com.smtm.users.registration

import com.smtm.users.api.UserRegistration

internal class UserRegistrationImpl : UserRegistration {

    override fun register(): UserProfile {
        return invalidUserProfileOf(mapOf(
                "email" to "e-mail is already registered",
                "password" to "password does not meet security policy"
        ))
    }
}

fun userRegistrationOf(): UserRegistration = UserRegistrationImpl()
