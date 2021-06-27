package com.smtm.application.security

import com.smtm.common.ConstraintViolation
import com.smtm.security.registration.invalidUserProfileOf
import io.cucumber.java.en.Given

class UserRegistrationStepdefs(private val fakeUserRegistration: FakeUserRegistration) {

    @Given("constraint violations are")
    fun constraintViolationsAre(constraintViolations: List<ConstraintViolation>) {
        fakeUserRegistration.userProfile = invalidUserProfileOf(constraintViolations)
    }
}
