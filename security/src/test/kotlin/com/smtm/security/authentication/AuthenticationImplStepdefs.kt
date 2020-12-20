package com.smtm.security.authentication

import com.smtm.security.World
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.*

class AuthenticationImplStepdefs(private val world: World) {

    private val authentication
        get() = authenticationOf(world.userRepository, world.tokenGenerationKey, world.tokenValidityTime, world.clock)

    private var token: Token? = null

    @When("user authenticates with email \"{emailAddress}\" and password \"{unencryptedPassword}\"")
    fun `user authenticates with email xxx and password xxx`(emailAddress: EmailAddress, password: UnencryptedPassword) {
        token = authentication.authenticate(emailAddress, password)
    }

    @Then("authorization token is empty")
    fun `authorization token is empty`() {
        assertThat(token).isNull()
    }

    @Then("authorization token is not empty")
    fun `authorization token is not empty`() {
        assertThat(token).isNotNull
    }

    @Then("user id is {int}")
    fun `user id is xxx`(id: Long) {
        assertThat(token?.userId).isEqualTo(id)
    }
}
