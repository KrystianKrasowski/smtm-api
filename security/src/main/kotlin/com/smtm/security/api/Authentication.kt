package com.smtm.security.api

import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword

interface CredentialsAuthentication {

    fun authenticate(emailAddress: EmailAddress, password: UnencryptedPassword): TokenPair?
}

interface RefreshTokenAuthentication {

    fun authenticate(token: String): TokenPair?
}

data class TokenPair internal constructor(val accessToken: AccessToken, val refreshToken: RefreshToken)

interface AccessToken {

    val userId: Long

    override fun toString(): String
}

interface RefreshToken {

    val userId: Long
    val tokenId: String

    override fun toString(): String
}

fun tokenPairOf(accessToken: AccessToken, refreshToken: RefreshToken) = TokenPair(accessToken, refreshToken)
