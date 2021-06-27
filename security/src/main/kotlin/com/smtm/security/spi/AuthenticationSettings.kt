package com.smtm.security.spi

interface AuthenticationSettings {

    val secret: String
    val accessTokenValidTime: Int
    val refreshTokenValidTime: Int
}
