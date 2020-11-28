package com.smtm.users.api

import com.smtm.users.registration.UnsecuredPassword
import com.smtm.users.registration.UserProfile

interface UserRegistration {

    fun register(email: String, password: UnsecuredPassword): UserProfile
}
