package com.smtm.security.authorization

import com.smtm.security.api.Authorization
import com.smtm.security.authentication.Token
import com.smtm.security.authentication.tokenOf

internal class AuthorizationImpl(private val secret: String) : Authorization {

    override fun authorize(token: String): Token? = runCatching { tokenOf(token, secret) }
            .getOrNull()
}

fun authorizationOf(secret: String): Authorization = AuthorizationImpl(secret)
