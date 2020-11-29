package com.smtm.users.api

import com.smtm.users.registration.UnencryptedPassword
import com.smtm.users.registration.UserProfile

interface UserRegistration {

    fun register(email: String, password: UnencryptedPassword): UserProfile
}
