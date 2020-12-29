package com.smtm.security.authorization

import com.smtm.security.api.Authorization
import com.smtm.security.authentication.createToken
import com.smtm.security.token.Token

internal class AuthorizationImpl(private val secret: String) : Authorization {

    override fun authorize(token: String): Token? = createToken(token, secret)
}

fun authorizationOf(secret: String): Authorization = AuthorizationImpl(secret)
