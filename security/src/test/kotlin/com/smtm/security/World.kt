package com.smtm.security

import com.smtm.security.authentication.JwtTokenFactory
import com.smtm.security.spi.FakeAuthenticationSettings
import com.smtm.security.spi.FakeGuidGenerator
import com.smtm.security.spi.FakeRefreshTokensRepository
import com.smtm.security.spi.FakeUserRepository
import java.time.Clock

class World {

    var userRepository = FakeUserRepository()
    var authenticationSettings = FakeAuthenticationSettings()
    var clock = Clock.systemUTC()!!

    val refreshTokenRepository = FakeRefreshTokensRepository()
    val guidGenerator = FakeGuidGenerator()
    val tokenFactory: JwtTokenFactory
        get() = JwtTokenFactory(authenticationSettings, clock, guidGenerator)
}
