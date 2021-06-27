package com.smtm.security.authentication

import com.smtm.security.World
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.token.Token
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat

class AuthenticationImplStepdefs(private val world: World) {

    private val authentication
        get() = authenticationOf(world.userRepository, world.tokenGenerationKey, world.tokenValidityTime, world.clock)

    private var token: Token? = null

    @When("user authenticates with email \"{emailAddress}\" and password \"{unencryptedPassword}\"")
    fun `user authenticates with email xxx and password xxx`(emailAddress: EmailAddress, password: UnencryptedPassword) {
        token = authentication.authenticate(emailAddress, password)
    }

    @Then("access token is empty")
    fun `access token is empty`() {
        assertThat(token).isNull()
    }

    @Then("access token is not empty")
    fun `access token is not empty`() {
        assertThat(token).isNotNull
    }

    @Then("user id is {int}")
    fun `user id is xxx`(id: Long) {
        assertThat(token?.userId).isEqualTo(id)
    }
}
