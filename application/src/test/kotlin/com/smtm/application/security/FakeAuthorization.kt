package com.smtm.application.security

import com.smtm.security.api.Authorization
import com.smtm.security.api.Token

class FakeAuthorization : Authorization {

    val validTokens: MutableList<String> = mutableListOf()

    override fun authorize(token: String): Token? = token
        .takeIf { validTokens.contains(it) }
        ?.let { tokenOf(it) }
}
