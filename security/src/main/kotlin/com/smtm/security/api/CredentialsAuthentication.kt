package com.smtm.security.api

import com.smtm.security.authentication.Tokens
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword

interface CredentialsAuthentication {

    fun authenticate(emailAddress: EmailAddress, password: UnencryptedPassword): Tokens?
}
