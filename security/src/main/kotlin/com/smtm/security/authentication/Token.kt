package com.smtm.security.authentication

data class Tokens internal constructor(val accessToken: Token, val refreshToken: Token)

data class Token internal constructor(val value: String, val userId: Long) {

    override fun toString() = value
}

fun tokensOf(accessToken: Token, refreshToken: Token) = Tokens(accessToken, refreshToken)

fun tokenOf(token: String, userId: Long) = Token(token, userId)
