package com.smtm.users.spi

import com.smtm.users.registration.Password
import com.smtm.users.registration.UserProfile
import com.smtm.users.registration.validUserProfileOf

class FakeUserRepository : UsersRepository {

    private val userList = mutableListOf<UserProfile.Valid>()

    override fun register(email: String, password: Password): UserProfile = validUserProfileOf(generateId(), email)

    override fun hasNoEmailAs(email: String): Boolean = !userList.any { it.email == email }

    fun addUsers(users: List<UserProfile.Valid>) {
        userList.addAll(users)
    }

    private fun generateId() = userList.size.toLong()
}
