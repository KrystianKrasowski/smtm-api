package com.smtm.users.registration

import com.smtm.users.World
import com.smtm.users.api.UserRegistration
import com.smtm.users.assertThat
import io.cucumber.java.ParameterType
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class UserRegistrationImplStepdefs(private val world: World) {

    private val userRegistration: UserRegistration
        get() = userRegistrationOf(world.userRepository, world.passwordEncryption)

    private var userProfile: UserProfile? = null

    @When("user registers as {string} with password \"{unsecuredPassword}\"")
    fun `user registers as arg1 with password arg2`(email: String, password: UnsecuredPassword) {
        userProfile = userRegistration.register(email, password)
    }

    @Then("user {string} is registered")
    fun `user arg1 is registered`(email: String) {
        assertThat(userProfile)
                .hasEmail(email)
    }

    @ParameterType(".*")
    fun unsecuredPassword(input: String) = unsecuredPasswordOf(input)
}
