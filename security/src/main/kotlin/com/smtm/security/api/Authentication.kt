package com.smtm.security.api

import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword

interface CredentialsAuthentication {

    fun authenticate(emailAddress: EmailAddress, password: UnencryptedPassword): TokenPair?
}

interface RefreshTokenAuthentication {

    fun authenticate(token: String): TokenPair?
}

data class TokenPair internal constructor(val accessToken: Token, val refreshToken: Token)

interface Token {

    val userId: Long

    override fun toString(): String
}

fun tokenPairOf(accessToken: Token, refreshToken: Token) = TokenPair(accessToken, refreshToken)
