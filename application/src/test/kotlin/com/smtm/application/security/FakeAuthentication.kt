package com.smtm.application.security

import com.smtm.application.security.v1.CredentialsDto
import com.smtm.application.security.v1.credentialsDtoOf
import com.smtm.security.api.Authentication
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.token.Token
import com.smtm.security.token.tokenOf

class FakeAuthentication : Authentication {

    val validTokens: MutableMap<CredentialsDto, Token> = mutableMapOf()

    override fun authenticate(emailAddress: EmailAddress, password: UnencryptedPassword): Token? = credentialsDtoOf(emailAddress, password)
            .let { validTokens[it] }
}
