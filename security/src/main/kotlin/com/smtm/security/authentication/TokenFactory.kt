package com.smtm.security.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.smtm.security.spi.AuthenticationSettings
import java.time.Clock
import java.time.Instant
import java.util.*
import com.auth0.jwt.interfaces.Clock as Auth0Clock

class TokenFactory(private val authenticationSettings: AuthenticationSettings, private val clock: Clock) {

    private val algorithm: Algorithm
        get() = Algorithm.HMAC512(authenticationSettings.secret)

    private val auth0Clock: Auth0Clock
        get() {
            return com.auth0.jwt.interfaces.Clock { Date.from(clock.instant()) }
        }

    internal fun createAccessToken(subject: Long) = create(subject, getExpirationDate(authenticationSettings.accessTokenValidTime))

    internal fun createRefreshToken(subject: Long) = create(subject, getExpirationDate(authenticationSettings.refreshTokenValidTime))

    internal fun create(subject: Long, expiresAt: Instant) = JWT.create()
        .withSubject(subject.toString())
        .withExpiresAt(Date.from(expiresAt))
        .sign(algorithm)
        .let { tokenOf(it, subject) }

    internal fun create(token: String) = JWT.require(algorithm)
        .let { it as JWTVerifier.BaseVerification }
        .build(auth0Clock)
        .runCatching { verify(token) }
        .map { tokenOf(token, it.subject.toLong()) }
        .getOrNull()

    private fun getExpirationDate(validityTime: Int) = clock
        .instant()
        .plusSeconds(validityTime.toLong())
}
