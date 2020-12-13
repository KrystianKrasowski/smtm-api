package com.smtm.security.api

import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.registration.UserProfile

interface UserRegistration {

    fun register(email: EmailAddress, password: UnencryptedPassword): UserProfile
}
