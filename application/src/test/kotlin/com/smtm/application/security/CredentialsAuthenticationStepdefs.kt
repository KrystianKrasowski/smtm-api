package com.smtm.application.security

import com.smtm.application.security.v1.CredentialsDto
import com.smtm.application.security.v1.credentialsDtoOf
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given

class CredentialsAuthenticationStepdefs(private val fakeAuthentication: FakeCredentialsAuthentication) {

    @Given("new access token {string} is produced by credentials")
    fun accessTokenIsProducedByCredentials(token: String, credentials: CredentialsDto) {
        fakeAuthentication.validAccessTokens[credentials] = tokenOf(token)
    }

    @Given("new refresh token {string} is produced by credentials")
    fun refreshTokenIsProducedByCredentials(token: String, credentials: CredentialsDto) {
        fakeAuthentication.validRefreshTokens[credentials] = tokenOf(token)
    }

    @DataTableType
    fun credentialsDto(credentials: Map<String, String>): CredentialsDto {
        return credentialsDtoOf(credentials.getValue("email"), credentials.getValue("password"))
    }
}
