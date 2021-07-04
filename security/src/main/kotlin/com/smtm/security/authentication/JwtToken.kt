package com.smtm.security.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.smtm.security.api.Token
import java.time.Instant
import java.util.*

internal data class JwtToken(private val subject: Long, private val expiresAt: Instant, private val id: String?, private val secret: String): Token {

    override val userId: Long
        get() = subject

    override fun toString(): String = JWT.create()
        .withSubject(subject.toString())
        .withExpiresAt(Date.from(expiresAt))
        .withJWTId(id)
        .sign(Algorithm.HMAC512(secret))
}
