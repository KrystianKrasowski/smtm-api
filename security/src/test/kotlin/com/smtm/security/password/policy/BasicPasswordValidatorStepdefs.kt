package com.smtm.security.password.policy

import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.registration.Violation
import com.smtm.security.registration.unencryptedPasswordOf
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat

class BasicPasswordValidatorStepdefs {

    private var specialCharacters: String = "."

    private var minimumLength: Int = 999

    private lateinit var password: UnencryptedPassword

    private lateinit var violations: Collection<Violation>

    @Given("special characters are {string}")
    fun `special characters are`(characters: String) {
        specialCharacters = characters
    }

    @Given("minimum length is {int}")
    fun `minimum length is`(length: Int) {
        minimumLength = length
    }

    @When("password {string} is validated")
    fun `password is validated`(value: String) {
        password = unencryptedPasswordOf(value)
        val validator = basicPasswordValidatorOf(
                specialCharacters = specialCharacters,
                minimumLength = minimumLength
        )
        violations = password.getViolations(validator)
    }

    @Then("password has {passwordViolation}")
    fun `password has violation`(violation: Violation) {
        assertThat(violations).contains(violation)
    }

    @Then("password has no violations")
    fun `password has no violations`() {
        assertThat(violations).isEmpty()
    }

}
