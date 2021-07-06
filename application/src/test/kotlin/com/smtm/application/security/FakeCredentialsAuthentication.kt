package com.smtm.application.security

import com.smtm.application.security.v1.CredentialsDto
import com.smtm.application.security.v1.credentialsDtoOf
import com.smtm.security.api.*
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword

class FakeCredentialsAuthentication : CredentialsAuthentication {

    val validAccessTokens: MutableMap<CredentialsDto, AccessToken> = mutableMapOf()
    val validRefreshTokens: MutableMap<CredentialsDto, RefreshToken> = mutableMapOf()

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
