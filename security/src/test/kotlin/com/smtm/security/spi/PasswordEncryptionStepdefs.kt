package com.smtm.security.spi

import com.smtm.security.World
import io.cucumber.java.en.Given

class PasswordEncryptionStepdefs(private val world: World) {

    @Given("password {string} encrypts to {string}")
    fun `password encrypts to`(raw: String, encrypted: String) {
        world.passwordEncryption.putEncryption(raw, encrypted)
    }
}
