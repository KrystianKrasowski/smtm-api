package com.smtm.application.security

import com.smtm.application.security.v1.CredentialsDto
import com.smtm.application.security.v1.credentialsDtoOf
import com.smtm.security.token.tokenOf
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given

class AuthenticationStepdefs(private val fakeAuthentication: FakeAuthentication) {

    @Given("token {string} is produced by credentials")
    fun tokenIsProducedByCredentials(token: String, credentials: CredentialsDto) {
        fakeAuthentication.validTokens[credentials] = tokenOf(token, 1)
    }

    @DataTableType
    fun credentialsDto(credentials: Map<String, String>): CredentialsDto {
        return credentialsDtoOf(credentials.getValue("email"), credentials.getValue("password"))
    }
}
