package com.smtm.application.security

import com.smtm.security.authentication.tokenOf
import io.cucumber.java.en.Given

class RefreshTokenAuthenticationStepdefs(private val authentication: FakeRefreshTokenAuthentication) {

    @Given("new access token {string} is produced by refresh token {string}")
    fun `new access token is produced by refresh token`(accessToken: String, previousRefreshToken: String) {
        authentication.accessTokens[previousRefreshToken] = tokenOf(accessToken, 1)
    }

    @Given("new refresh token {string} is produced by refresh token {string}")
    fun `new refresh token is produced by refresh token`(refreshToken: String, previousRefreshToken: String) {
        authentication.refreshTokens[previousRefreshToken] = tokenOf(refreshToken, 1)
    }

    @Given("invalid refresh token is {string}")
    fun `invalid refresh token is`(token: String) {
        authentication.invalidRefreshToken = token
    }
}
