package com.smtm.security.registration

import com.smtm.security.World
import com.smtm.security.api.UserRegistration
import com.smtm.security.assertThat
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

    @Then("email is not unique")
    fun `email is not unique`() {
        assertThat(userProfile).hasConstraintViolation("email", Violation.NonUnique)
    }

    @Then("password is not secure")
    fun `password is not secure`() {
        assertThat(userProfile).hasAnyConstraintViolationFor("password")
    }

    @Then("email is not valid")
    fun `email is not valid`() {
        assertThat(userProfile).hasConstraintViolation("email", Violation.NotAnEmailAddress)
    }
}
