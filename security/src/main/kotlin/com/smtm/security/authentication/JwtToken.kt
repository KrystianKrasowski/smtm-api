package com.smtm.security.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.smtm.security.api.AccessToken
import com.smtm.security.api.RefreshToken
import java.time.Instant
import java.util.*

internal data class JwtAccessToken(private val subject: Long, private val expiresAt: Instant, private val secret: String) : AccessToken {

    override val userId: Long
        get() = subject

    override fun toString(): String = JWT.create()
        .withSubject(subject.toString())
        .withExpiresAt(Date.from(expiresAt))
        .sign(Algorithm.HMAC512(secret))
}

internal data class JwtRefreshToken(
    private val subject: Long,
    private val expiresAt: Instant,
    private val id: String,
    private val secret: String
) : RefreshToken {

    override val userId: Long
        get() = subject

    override val tokenId: String
        get() = id

    override fun toString(): String = JWT.create()
        .withSubject(subject.toString())
        .withExpiresAt(Date.from(expiresAt))
        .withJWTId(id)
        .sign(Algorithm.HMAC512(secret))
}
