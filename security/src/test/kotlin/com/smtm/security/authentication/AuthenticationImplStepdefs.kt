package com.smtm.security.authentication

import com.smtm.security.World
import com.smtm.security.api.Token
import com.smtm.security.api.TokenPair
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat

class AuthenticationImplStepdefs(private val world: World) {

    private val authentication
        get() = AuthenticationImpl(world.userRepository, world.tokenFactory)

    private var tokenPair: TokenPair? = null

    @When("user authenticates with email \"{emailAddress}\" and password \"{unencryptedPassword}\"")
    fun `user authenticates with email xxx and password xxx`(emailAddress: EmailAddress, password: UnencryptedPassword) {
        tokenPair = authentication.authenticate(emailAddress, password)
    }

    @When("user authenticates with refresh token")
    fun `user authenticates with token`(token: Token) {
        tokenPair = authentication.authenticate(token.toString())
    }

    @Then("access token is empty")
    fun `access token is empty`() {
        assertThat(tokenPair?.accessToken).isNull()
    }

    @Then("refresh token is empty")
    fun `refresh token is empty`() {
        assertThat(tokenPair?.refreshToken).isNull()
    }

    @Then("access token is")
    fun `access token is`(token: Token) {
        assertThat(tokenPair?.accessToken).isEqualTo(token)
    }

    @Then("refresh token is")
    fun `refresh token is`(token: Token) {
        assertThat(tokenPair?.refreshToken).isEqualTo(token)
    }
}
