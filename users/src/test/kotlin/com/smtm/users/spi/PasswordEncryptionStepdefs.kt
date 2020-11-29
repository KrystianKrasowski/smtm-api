package com.smtm.users.spi

import com.smtm.users.World
import io.cucumber.java.en.Given

class PasswordEncryptionStepdefs(private val world: World) {

    @Given("password {string} encrypts to {string}")
    fun `password encrypts to`(raw: String, encrypted: String) {
        world.passwordEncryption.putEncryption(raw, encrypted)
    }
}
