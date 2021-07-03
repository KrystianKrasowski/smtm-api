package com.smtm.security.api

import com.smtm.security.authentication.Tokens

interface RefreshTokenAuthentication {

    fun authenticate(token: String): Tokens?
}
