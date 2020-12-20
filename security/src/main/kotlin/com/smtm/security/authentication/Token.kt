package com.smtm.security.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.*

data class Token internal constructor(val value: String, val secret: String) {

    val userId: Long
        get() = decodedToken.subject.toLong()

    private val decodedToken: DecodedJWT
        get() = JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(value)
}

fun tokenOf(subject: Long, expiresAt: Date, secret: String) = JWT.create()
        .withSubject(subject.toString())
        .withExpiresAt(expiresAt)
        .sign(algorithm(secret))
        .let { tokenOf(it, secret) }

fun tokenOf(value: String, secret: String) = Token(value, secret)

private fun algorithm(secret: String) = Algorithm.HMAC512(secret)
