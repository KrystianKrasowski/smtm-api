package com.smtm.security

import com.smtm.security.authentication.JwtTokenFactory
import com.smtm.security.spi.FakeAuthenticationSettings
import com.smtm.security.spi.FakeUserRepository
import java.time.Clock

class World {

    var userRepository = FakeUserRepository()
    var authenticationSettings = FakeAuthenticationSettings()
    var clock = Clock.systemUTC()

    val tokenFactory: JwtTokenFactory
        get() = JwtTokenFactory(authenticationSettings, clock)
}
