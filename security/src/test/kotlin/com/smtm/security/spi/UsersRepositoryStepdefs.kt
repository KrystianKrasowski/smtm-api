package com.smtm.security.spi

import com.smtm.security.ValidUserProfileEntry
import com.smtm.security.World
import com.smtm.security.registration.validUserProfileOf
import io.cucumber.java.en.Given

class UsersRepositoryStepdefs(private val world: World) {

    @Given("users repository contains")
    fun `user repository contains valid`(profiles: List<ValidUserProfileEntry>) {
        profiles.forEach {
            world.userRepository.addUser(it.emailAddress, it.password, validUserProfileOf(it.id, it.emailAddress))
        }
    }
}
