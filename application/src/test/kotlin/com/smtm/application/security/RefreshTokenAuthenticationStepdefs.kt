package com.smtm.application.security

import io.cucumber.java.en.Given

class RefreshTokenAuthenticationStepdefs(private val authentication: FakeRefreshTokenAuthentication) {

    @Given("new access token {string} is produced by refresh token {string}")
    fun `new access token is produced by refresh token`(accessToken: String, previousRefreshToken: String) {
        authentication.accessTokens[previousRefreshToken] = tokenOf(accessToken)
    }

    @Given("new refresh token {string} is produced by refresh token {string}")
    fun `new refresh token is produced by refresh token`(refreshToken: String, previousRefreshToken: String) {
        authentication.refreshTokens[previousRefreshToken] = tokenOf(refreshToken)
    }

    @Given("invalid refresh token is {string}")
    fun `invalid refresh token is`(token: String) {
        authentication.invalidRefreshToken = token
    }
}
