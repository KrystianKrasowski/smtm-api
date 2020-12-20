package com.smtm.security.spi

import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.registration.UserProfile
import com.smtm.security.registration.validUserProfileOf

class FakeUserRepository : UsersRepository {

    private val users = mutableMapOf<String, UserProfile.Valid>()

    lateinit var registeredEmail: EmailAddress

    lateinit var registeredPassword: UnencryptedPassword

    override fun register(email: EmailAddress, password: UnencryptedPassword): UserProfile {
        registeredEmail = email
        registeredPassword = password
        return validUserProfileOf(generateId(), email)
    }

    override fun isRegistered(email: EmailAddress): Boolean = users.values.any { it.email == email }

    override fun findAuthorized(email: EmailAddress, password: UnencryptedPassword): UserProfile.Valid? {
        return users["$email.$password"]
    }

    fun addUser(email: EmailAddress, password: UnencryptedPassword, profile: UserProfile.Valid) {
        users["$email.$password"] = profile
    }

    private fun generateId() = users.size.toLong()
}
