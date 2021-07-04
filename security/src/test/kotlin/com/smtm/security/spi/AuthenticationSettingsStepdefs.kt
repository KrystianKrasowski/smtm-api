package com.smtm.security.spi

import com.smtm.security.World
import io.cucumber.java.en.Given

class AuthenticationSettingsStepdefs(private val world: World) {

    @Given("access token validity time is {int} minutes")
    fun `access token validity time is nn minutes`(minutes: Int) {
        world.authenticationSettings.accessTokenValidTime = minutes * 60
    }

    @Given("refresh token validity time is {int} minutes")
    fun `refresh token validity time is nn minutes`(minutes: Int) {
        world.authenticationSettings.refreshTokenValidTime = minutes * 60
    }
}
