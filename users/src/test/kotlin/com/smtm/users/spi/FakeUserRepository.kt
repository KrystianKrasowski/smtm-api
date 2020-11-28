package com.smtm.users.spi

import com.smtm.users.registration.Password
import com.smtm.users.registration.UserProfile
import com.smtm.users.registration.validUserProfileOf

class FakeUserRepository : UsersRepository {

    private val userList = mutableListOf<UserProfile>()

    override fun register(email: String, password: Password): UserProfile {
        return validUserProfileOf(generateId(), email)
    }

    fun addUsers(users: List<UserProfile>) {
        userList.addAll(users)
    }

    private fun generateId() = userList.size.toLong()
}
