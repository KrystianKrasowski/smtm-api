package com.smtm.security.api

import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.authentication.Token

interface Authentication {

    fun authenticate(emailAddress: EmailAddress, password: UnencryptedPassword): Token?
}
