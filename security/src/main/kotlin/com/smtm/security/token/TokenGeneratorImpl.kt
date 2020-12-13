package com.smtm.security.token

import com.smtm.security.api.TokenGenerator
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword

internal class TokenGeneratorImpl : TokenGenerator {

    override fun generate(emailAddress: EmailAddress, password: UnencryptedPassword): Token? = tokenOf("123456789")
}

fun tokenGeneratorOf(): TokenGenerator = TokenGeneratorImpl()
