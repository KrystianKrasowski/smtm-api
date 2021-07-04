package com.smtm.application.security

import com.smtm.security.api.RefreshTokenAuthentication
import com.smtm.security.authentication.Token
import com.smtm.security.authentication.Tokens
import com.smtm.security.authentication.tokensOf

class FakeRefreshTokenAuthentication : RefreshTokenAuthentication {


    val accessTokens = mutableMapOf<String, Token>()
    val refreshTokens = mutableMapOf<String, Token>()
    var invalidRefreshToken = ""

    override fun authenticate(token: String): Tokens? {
        if (token == invalidRefreshToken) {
            return null
        }
        return tokensOf(accessTokens.getValue(token), refreshTokens.getValue(token))
    }
}
