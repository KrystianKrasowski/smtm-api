package com.smtm.security.authentication

import com.smtm.security.World
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat

class AuthenticationImplStepdefs(private val world: World) {

    private val authentication
        get() = AuthenticationImpl(world.userRepository, world.tokenFactory)

    private var tokens: Tokens? = null

    @When("user authenticates with email \"{emailAddress}\" and password \"{unencryptedPassword}\"")
    fun `user authenticates with email xxx and password xxx`(emailAddress: EmailAddress, password: UnencryptedPassword) {
        tokens = authentication.authenticate(emailAddress, password)
    }

    @When("user authenticates with refresh token")
    fun `user authenticates with token`(token: String) {
        tokens = authentication.authenticate(token)
    }

    @Then("access token is empty")
    fun `access token is empty`() {
        assertThat(tokens?.accessToken).isNull()
    }

    @Then("refresh token is empty")
    fun `refresh token is empty`() {
        assertThat(tokens?.refreshToken).isNull()
    }

    @Then("access token is")
    fun `access token is`(token: Token) {
        assertThat(tokens?.accessToken).isEqualTo(token)
    }

    @Then("refresh token is")
    fun `refresh token is`(token: Token) {
        assertThat(tokens?.refreshToken).isEqualTo(token)
    }
}
