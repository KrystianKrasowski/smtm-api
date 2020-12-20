package com.smtm.security.api

import com.smtm.security.authentication.Token

interface Authorization {

    fun authorize(token: String): Token?
}
