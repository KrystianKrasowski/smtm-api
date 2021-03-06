package com.smtm.security.spi

import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.registration.UserProfile

interface UsersRepository {

    fun register(email: EmailAddress, password: UnencryptedPassword): UserProfile

    fun isRegistered(email: EmailAddress): Boolean

    fun isRegistered(userId: Long): Boolean

    fun findAuthorized(email: EmailAddress, password: UnencryptedPassword): UserProfile.Valid?
}
