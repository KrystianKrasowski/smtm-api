package com.smtm.users.registration

import com.smtm.users.World
import com.smtm.users.api.UserRegistration
import com.smtm.users.assertThat
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

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
    }

    @Then("the uniqueness of the email address has been violated")
    fun `the uniqueness of the email address has been violated`() {
        assertThat(userProfile).hasConstraintViolation("email", Violation.NonUnique)
    }

    @Then("the security policy of the password has been violated")
    fun `the security policy of the password has been violated`() {
        assertThat(userProfile).hasAnyConstraintViolationFor("password")
    }

    @Then("email is not valid")
    fun `email is not valid`() {
        assertThat(userProfile).hasConstraintViolation("email", Violation.NotAnEmailAddress)
    }
}
