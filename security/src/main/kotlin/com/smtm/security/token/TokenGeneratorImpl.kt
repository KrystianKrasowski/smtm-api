package com.smtm.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.smtm.security.api.TokenGenerator
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import java.time.Clock
import java.time.Instant
import java.util.*

internal class TokenGeneratorImpl(
        private val secret: String,
        private val validityTime: Int,
        private val clock: Clock
) : TokenGenerator {

    override fun generate(emailAddress: EmailAddress, password: UnencryptedPassword): Token? {
        return JWT.create()
                .withSubject(emailAddress.toString())
                .withExpiresAt(Date.from(specifyExpirationDate()))
                .sign(Algorithm.HMAC512(secret))
                .let { tokenOf(it) }
    }

    private fun specifyExpirationDate(): Instant {
        return clock.instant().plusSeconds(validityTime.toLong())
    }
}

fun tokenGeneratorOf(secret: String, validityTime: Int, clock: Clock): TokenGenerator = TokenGeneratorImpl(secret, validityTime, clock)
