package com.smtm.application.security

import com.smtm.security.api.UserRegistration
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.registration.UserProfile
import com.smtm.security.registration.validUserProfileOf

class FakeUserRegistration : UserRegistration {

    var userProfile: UserProfile? = null

    override fun register(email: EmailAddress, password: UnencryptedPassword): UserProfile = userProfile ?: validUserProfileOf(1, email)
}
