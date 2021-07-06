package com.smtm.security.authentication

import com.smtm.security.api.*
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.spi.RefreshTokensRepository
import com.smtm.security.spi.UsersRepository

class AuthenticationImpl(
    private val usersRepository: UsersRepository,
    private val tokenFactory: JwtTokenFactory,
    private val refreshTokensRepository: RefreshTokensRepository
) : CredentialsAuthentication, RefreshTokenAuthentication {

    override fun authenticate(emailAddress: EmailAddress, password: UnencryptedPassword): TokenPair? = usersRepository
        .findAuthorized(emailAddress, password)
        ?.let { createTokenPair(it.id) }
        ?.also { it.refreshToken.save() }

    override fun authenticate(token: String): TokenPair? = tokenFactory.createRefreshToken(token)
        ?.takeIf { usersRepository.isRegistered(it.userId) }
        ?.takeIf { refreshTokensRepository.exists(it.userId, it.tokenId) }
        ?.let { createTokenPair(it.userId) }
        ?.also { it.refreshToken.save() }

    private fun createTokenPair(userId: Long) = tokenPairOf(
        accessToken = tokenFactory.createAccessToken(userId),
        refreshToken = tokenFactory.createRefreshToken(userId)
    )

    private fun RefreshToken.save() = refreshTokensRepository.save(userId, tokenId)
}
