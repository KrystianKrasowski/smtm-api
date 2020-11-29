package com.smtm.users.registration

import com.smtm.users.World
import com.smtm.users.api.UserRegistration
import com.smtm.users.assertThat
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*

class UserRegistrationImplStepdefs(private val world: World) {

    private val userRegistration: UserRegistration
        get() = userRegistrationOf(world.userRepository, world.passwordEncryption)

    private var userProfile: UserProfile? = null

    @When("user registers as \"{emailAddress}\" with password \"{unsecuredPassword}\"")
    fun `user registers as arg1 with password arg2`(email: EmailAddress, password: UnencryptedPassword) {
        userProfile = userRegistration.register(email, password)
    }

    @Then("user \"{emailAddress}\" is registered")
    fun `user arg1 is registered`(email: EmailAddress) {
        assertThat(userProfile).hasEmail(email)
        assertThat(world.userRepository.registeredEmail).isEqualTo(email)
    }

    @Then("password is encrypted to \"{password}\"")
    fun `password is encrypted to`(password: Password) {
        assertThat(world.userRepository.registeredPassword).isEqualTo(password)
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
