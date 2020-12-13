package com.smtm.security.api

import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.token.Token

interface TokenGenerator {

    fun generate(emailAddress: EmailAddress, password: UnencryptedPassword): Token?
}
