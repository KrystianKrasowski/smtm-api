package com.smtm.security.registration

import com.smtm.security.World
import com.smtm.security.api.UserRegistration
import com.smtm.security.assertThat
import io.cucumber.java.ParameterType
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat

class UserRegistrationImplStepdefs(private val world: World) {

    private val userRegistration: UserRegistration
        get() = userRegistrationOf(world.userRepository)

    private var userProfile: UserProfile? = null

    @When("user registers as \"{emailAddress}\" with password \"{unencryptedPassword}\"")
    fun `user registers as arg1 with password arg2`(email: EmailAddress, password: UnencryptedPassword) {
        userProfile = userRegistration.register(email, password)
    }

    @Then("user \"{emailAddress}\" is registered")
    fun `user arg1 is registered`(email: EmailAddress) {
        assertThat(userProfile).hasEmail(email)
        assertThat(world.userRepository.registeredEmail).isEqualTo(email)
    }

    @Then("message for {string} violation is {string} with parameters {violationMessageParameters}")
    fun `message for xxx violation is yyy with parameters zzz`(property: String, pattern: String, parameters: Map<String, String>) {
        assertThat(userProfile).hasConstraintViolation(property, pattern, parameters)
    }

    @ParameterType("(([A-Za-z0-9-_ .,]+=[A-Za-z0-9-_ .,@]+(,)?)+)")
    fun violationMessageParameters(input: String) = input
        .split(",")
        .map { it.trim() }
        .map { parameter ->
            parameter
                .split("=")
                .let { Pair(it[0], it[1]) }
        }
        .toMap()
}
