package com.smtm.security.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

internal fun createToken(subject: Long, expiresAt: Date, secret: String) = JWT.create()
    .withSubject(subject.toString())
    .withExpiresAt(expiresAt)
    .sign(algorithm(secret))
    .let { tokenOf(it, subject) }

internal fun createToken(token: String, secret: String) = JWT.require(algorithm(secret))
    .build()
    .runCatching { verify(token) }
    .map { tokenOf(token, it.subject.toLong()) }
    .getOrNull()

private fun algorithm(secret: String) = Algorithm.HMAC512(secret)
