package com.smtm.application.security

import com.smtm.application.security.v1.CredentialsDto
import com.smtm.application.security.v1.credentialsDtoOf
import com.smtm.security.api.Authentication
import com.smtm.security.authentication.Token
import com.smtm.security.authentication.Tokens
import com.smtm.security.authentication.tokensOf
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword

class FakeAuthentication : Authentication {

    val validAccessTokens: MutableMap<CredentialsDto, Token> = mutableMapOf()
    val validRefreshTokens: MutableMap<CredentialsDto, Token> = mutableMapOf()

    override fun authenticate(emailAddress: EmailAddress, password: UnencryptedPassword): Tokens? = credentialsDtoOf(emailAddress, password)
        .let {
            if (validAccessTokens.containsKey(it) && validRefreshTokens.containsKey(it)) {
                tokensOf(
                    accessToken = validAccessTokens.getValue(it),
                    refreshToken = validRefreshTokens.getValue(it)
                )
            } else {
                null
            }
        }
}
