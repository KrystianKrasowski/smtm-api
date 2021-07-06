package com.smtm.security.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.smtm.security.spi.AuthenticationSettings
import com.smtm.security.spi.GuidGenerator
import java.time.Clock
import java.time.Instant
import java.util.*
import com.auth0.jwt.interfaces.Clock as Auth0Clock

class JwtTokenFactory(private val authenticationSettings: AuthenticationSettings,
                      private val clock: Clock,
                      private val guidGenerator: GuidGenerator) {

    private val algorithm: Algorithm
        get() = Algorithm.HMAC512(authenticationSettings.secret)

    private val auth0Clock: Auth0Clock
        get() {
            return com.auth0.jwt.interfaces.Clock { Date.from(clock.instant()) }
        }

    internal fun createAccessToken(subject: Long) = createAccessToken(subject, getExpirationDate(authenticationSettings.accessTokenValidTime))

    internal fun createAccessToken(token: String) = JWT.require(algorithm)
        .let { it as JWTVerifier.BaseVerification }
        .build(auth0Clock)
        .runCatching { verify(token) }
        .map { createAccessToken(it.subject.toLong(), it.expiresAt.toInstant()) }
        .getOrNull()

    internal fun createAccessToken(subject: Long, expiresAt: Instant) = JwtAccessToken(
        subject = subject,
        expiresAt = expiresAt,
        secret = authenticationSettings.secret
    )

    internal fun createRefreshToken(subject: Long) = createRefreshToken(subject, getExpirationDate(authenticationSettings.refreshTokenValidTime), guidGenerator.generate())

    internal fun createRefreshToken(token: String) = JWT.require(algorithm)
        .let { it as JWTVerifier.BaseVerification }
        .build(auth0Clock)
        .runCatching { verify(token) }
        .map { createRefreshToken(it.subject.toLong(), it.expiresAt.toInstant(), it.id) }
        .getOrNull()

    internal fun createRefreshToken(subject: Long, expiresAt: Instant, id: String) = JwtRefreshToken(
        subject = subject,
        expiresAt = expiresAt,
        id = id,
        secret = authenticationSettings.secret
    )

    private fun getExpirationDate(validityTime: Int) = clock
        .instant()
        .plusSeconds(validityTime.toLong())
}
