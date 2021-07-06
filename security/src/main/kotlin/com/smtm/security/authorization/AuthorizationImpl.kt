package com.smtm.security.authorization

import com.smtm.security.api.Authorization
import com.smtm.security.api.AccessToken
import com.smtm.security.authentication.JwtTokenFactory

class AuthorizationImpl(private val tokenFactory: JwtTokenFactory) : Authorization {

    override fun authorize(token: String): AccessToken? = tokenFactory.createAccessToken(token)
}
