package com.smtm.users.api

import com.smtm.users.registration.EmailAddress
import com.smtm.users.registration.UnencryptedPassword
import com.smtm.users.registration.UserProfile

interface UserRegistration {

    fun register(email: EmailAddress, password: UnencryptedPassword): UserProfile
}
