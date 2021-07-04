package com.smtm.application.security

import com.smtm.security.api.RefreshTokenAuthentication
import com.smtm.security.api.Token
import com.smtm.security.api.TokenPair
import com.smtm.security.api.tokenPairOf

class FakeRefreshTokenAuthentication : RefreshTokenAuthentication {


    val accessTokens = mutableMapOf<String, Token>()
    val refreshTokens = mutableMapOf<String, Token>()
    var invalidRefreshToken = ""

    override fun authenticate(token: String): TokenPair? {
        if (token == invalidRefreshToken) {
            return null
        }
        return tokenPairOf(accessTokens.getValue(token), refreshTokens.getValue(token))
    }
}
