package com.smtm.security.api

import com.smtm.security.token.Token

interface Authorization {

    fun authorize(token: String): Token?
}
