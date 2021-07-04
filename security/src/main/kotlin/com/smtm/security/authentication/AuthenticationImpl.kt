package com.smtm.security.authentication

import com.smtm.security.api.CredentialsAuthentication
import com.smtm.security.api.RefreshTokenAuthentication
import com.smtm.security.api.TokenPair
import com.smtm.security.api.tokenPairOf
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.spi.UsersRepository

class AuthenticationImpl(
    private val usersRepository: UsersRepository,
    private val tokenFactory: JwtTokenFactory
) : CredentialsAuthentication, RefreshTokenAuthentication {

    override fun authenticate(emailAddress: EmailAddress, password: UnencryptedPassword): TokenPair? = usersRepository
        .findAuthorized(emailAddress, password)
        ?.let { createTokens(it.id) }

    override fun authenticate(token: String): TokenPair? = tokenFactory.create(token)
        ?.takeIf { usersRepository.isRegistered(it.userId) }
        ?.let { createTokens(it.userId) }

    private fun createTokens(userId: Long) = tokenPairOf(
        accessToken = tokenFactory.createAccessToken(userId),
        refreshToken = tokenFactory.createRefreshToken(userId)
    )
}
