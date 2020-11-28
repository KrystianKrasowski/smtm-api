package com.smtm.users.api

import com.smtm.users.registration.UserProfile

interface UserRegistration {

    fun register(): UserProfile
}
