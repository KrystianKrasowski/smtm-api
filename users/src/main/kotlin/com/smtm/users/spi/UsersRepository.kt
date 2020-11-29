package com.smtm.users.spi

import com.smtm.users.registration.EmailAddress
import com.smtm.users.registration.Password
import com.smtm.users.registration.UserProfile

interface UsersRepository {

    fun register(email: EmailAddress, password: Password): UserProfile

    fun isRegistered(email: EmailAddress): Boolean
}
