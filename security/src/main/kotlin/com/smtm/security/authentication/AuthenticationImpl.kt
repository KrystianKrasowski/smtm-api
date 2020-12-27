package com.smtm.security.authentication

import com.smtm.security.api.Authentication
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.spi.UsersRepository
import com.smtm.security.token.Token
import java.time.Clock
import java.util.*

internal class AuthenticationImpl(
        private val usersRepository: UsersRepository,
        private val secret: String,
        private val validityTime: Int,
        private val clock: Clock
) : Authentication {

    override fun authenticate(emailAddress: EmailAddress, password: UnencryptedPassword): Token? = usersRepository
            .findAuthorized(emailAddress, password)
            ?.let { createToken(it.id, Date.from(specifyExpirationDate()), secret) }

    private fun specifyExpirationDate() = clock
            .instant()
            .plusSeconds(validityTime.toLong())
}

fun authenticationOf(usersRepository: UsersRepository,
                     secret: String,
                     validityTime: Int,
                     clock: Clock): Authentication = AuthenticationImpl(usersRepository, secret, validityTime, clock)
