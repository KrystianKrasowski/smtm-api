package com.smtm.security.authentication

import com.smtm.security.api.CredentialsAuthentication
import com.smtm.security.api.RefreshTokenAuthentication
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.spi.AuthenticationSettings
import com.smtm.security.spi.UsersRepository
import java.time.Clock
import java.util.*

class AuthenticationImpl(
    private val usersRepository: UsersRepository,
    private val tokenFactory: TokenFactory
) : CredentialsAuthentication, RefreshTokenAuthentication {

    override fun authenticate(emailAddress: EmailAddress, password: UnencryptedPassword): Tokens? = usersRepository
        .findAuthorized(emailAddress, password)
        ?.let { createTokens(it.id) }

    override fun authenticate(token: String): Tokens? = tokenFactory.create(token)
        ?.takeIf { usersRepository.isRegistered(it.userId) }
        ?.let { createTokens(it.userId) }

    private fun createTokens(userId: Long) = tokensOf(
        accessToken = tokenFactory.createAccessToken(userId),
        refreshToken = tokenFactory.createRefreshToken(userId)
    )
}
