package com.smtm.security.spi

class FakeAuthenticationSettings : AuthenticationSettings {

    override val secret = "aaa"

    override var accessTokenValidTime = 300

    override var refreshTokenValidTime = 900
}
