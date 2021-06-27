package com.smtm.security.authentication

import com.smtm.security.World
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat

class AuthenticationImplStepdefs(private val world: World) {

    private val authentication
        get() = authenticationOf(world.userRepository, world.authenticationSettings, world.clock)

    private var tokens: Tokens? = null

    @When("user authenticates with email \"{emailAddress}\" and password \"{unencryptedPassword}\"")
    fun `user authenticates with email xxx and password xxx`(emailAddress: EmailAddress, password: UnencryptedPassword) {
        tokens = authentication.authenticate(emailAddress, password)
    }

    @Then("access token is empty")
    fun `access token is empty`() {
        assertThat(tokens?.accessToken).isNull()
    }

    @Then("access token is not empty")
    fun `access token is not empty`() {
        assertThat(tokens?.accessToken).isNotNull
    }

    @Then("user id is {int}")
    fun `user id is xxx`(id: Long) {
        assertThat(tokens?.accessToken?.userId).isEqualTo(id)
    }
}
