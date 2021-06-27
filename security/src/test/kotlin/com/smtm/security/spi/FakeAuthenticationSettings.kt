package com.smtm.security.spi

class FakeAuthenticationSettings : AuthenticationSettings {

    override val secret = "aaa"

    override val accessTokenValidTime = 300

    override val refreshTokenValidTime = 900
}
