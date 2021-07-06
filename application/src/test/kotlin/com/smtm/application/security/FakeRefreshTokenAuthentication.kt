package com.smtm.application.security

import com.smtm.security.api.*

class FakeRefreshTokenAuthentication : RefreshTokenAuthentication {

    val accessTokens = mutableMapOf<String, AccessToken>()
    val refreshTokens = mutableMapOf<String, RefreshToken>()
    var invalidRefreshToken = ""

    override fun authenticate(token: String): TokenPair? {
        if (token == invalidRefreshToken) {
            return null
        }
        return tokenPairOf(accessTokens.getValue(token), refreshTokens.getValue(token))
    }
}
