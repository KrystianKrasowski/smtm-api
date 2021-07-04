package com.smtm.security.authorization

import com.smtm.security.api.Authorization
import com.smtm.security.authentication.Token
import com.smtm.security.authentication.TokenFactory

class AuthorizationImpl(private val tokenFactory: TokenFactory) : Authorization {

    override fun authorize(token: String): Token? = tokenFactory.create(token)
}
