package com.smtm.security.authentication

import com.smtm.security.api.CredentialsAuthentication
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.spi.AuthenticationSettings
import com.smtm.security.spi.UsersRepository
import java.time.Clock
import java.util.*

internal class CredentialsAuthenticationImpl(
    private val usersRepository: UsersRepository,
    private val authenticationSettings: AuthenticationSettings,
    private val clock: Clock
) : CredentialsAuthentication {

    override fun authenticate(emailAddress: EmailAddress, password: UnencryptedPassword): Tokens? = usersRepository
        .findAuthorized(emailAddress, password)
        ?.let {
            tokensOf(
                accessToken = createToken(it.id, authenticationSettings.accessTokenValidTime),
                refreshToken = createToken(it.id, authenticationSettings.refreshTokenValidTime)
            )
        }

    private fun createToken(userId: Long, validTime: Int) = createToken(userId, specifyExpirationDate(validTime), authenticationSettings.secret)

    private fun specifyExpirationDate(time: Int) = clock
        .instant()
        .plusSeconds(time.toLong())
        .let { Date.from(it) }
}

fun authenticationOf(
    usersRepository: UsersRepository,
    authenticationSettings: AuthenticationSettings,
    clock: Clock
): CredentialsAuthentication = CredentialsAuthenticationImpl(usersRepository, authenticationSettings, clock)
