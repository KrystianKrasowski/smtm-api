package com.smtm.application.security

import com.smtm.security.api.Authorization
import com.smtm.security.api.AccessToken

class FakeAuthorization : Authorization {

    val validTokens: MutableList<String> = mutableListOf()

    override fun authorize(token: String): AccessToken? = token
        .takeIf { validTokens.contains(it) }
        ?.let { accessTokenOf(it) }
}
