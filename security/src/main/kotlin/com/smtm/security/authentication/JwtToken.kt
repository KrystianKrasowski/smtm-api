package com.smtm.security.authentication

import com.smtm.security.api.Token

internal data class JwtToken(val subject: Long, private val value: String): Token {

    override val userId: Long
        get() = subject

    override fun toString() = value
}
