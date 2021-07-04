package com.smtm.application.security

import com.smtm.application.security.v1.CredentialsDto
import com.smtm.application.security.v1.credentialsDtoOf
import com.smtm.security.api.CredentialsAuthentication
import com.smtm.security.api.Token
import com.smtm.security.api.TokenPair
import com.smtm.security.api.tokenPairOf
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword

class FakeCredentialsAuthentication : CredentialsAuthentication {

    val validAccessTokens: MutableMap<CredentialsDto, Token> = mutableMapOf()
    val validRefreshTokens: MutableMap<CredentialsDto, Token> = mutableMapOf()

    override fun authenticate(emailAddress: EmailAddress, password: UnencryptedPassword): TokenPair? = credentialsDtoOf(emailAddress, password)
        .let {
            if (validAccessTokens.containsKey(it) && validRefreshTokens.containsKey(it)) {
                tokenPairOf(
                    accessToken = validAccessTokens.getValue(it),
                    refreshToken = validRefreshTokens.getValue(it)
                )
            } else {
                null
            }
        }
}
