package com.smtm.users.spi

import com.smtm.users.registration.Password
import com.smtm.users.registration.UserProfile

interface UsersRepository {

    fun register(email: String, password: Password): UserProfile
}
