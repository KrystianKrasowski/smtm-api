package com.smtm.users.spi

import com.smtm.users.registration.EmailAddress
import com.smtm.users.registration.Password
import com.smtm.users.registration.UserProfile
import com.smtm.users.registration.validUserProfileOf

class FakeUserRepository : UsersRepository {

    private val userList = mutableListOf<UserProfile.Valid>()

    lateinit var registeredEmail: EmailAddress

    lateinit var registeredPassword: Password

    override fun register(email: EmailAddress, password: Password): UserProfile {
        registeredEmail = email
        registeredPassword = password
        return validUserProfileOf(generateId(), email)
    }

    override fun isRegistered(email: EmailAddress): Boolean = userList.any { it.email == email }

    fun addUsers(users: List<UserProfile.Valid>) {
        userList.addAll(users)
    }

    private fun generateId() = userList.size.toLong()
}
