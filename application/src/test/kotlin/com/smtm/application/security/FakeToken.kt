package com.smtm.application.security

import com.smtm.security.api.AccessToken
import com.smtm.security.api.RefreshToken

class FakeAccessToken(override val userId: Long, private val token: String) : AccessToken {

    override fun toString(): String {
        return token
    }
}

class FakeRefreshToken(override val userId: Long, override val tokenId: String, private val token: String) : RefreshToken {

    override fun toString(): String {
        return token
    }
}

fun accessTokenOf(value: String) = FakeAccessToken(1, value)

fun refreshTokenOf(value: String) = FakeRefreshToken(1, "x", value)
