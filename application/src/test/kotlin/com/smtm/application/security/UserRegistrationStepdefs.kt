package com.smtm.application.security

import com.smtm.security.registration.*
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given

class UserRegistrationStepdefs(private val fakeUserRegistration: FakeUserRegistration) {

    @Given("constraint violations are")
    fun constraintViolationsAre(constraintViolations: List<ConstraintViolation>) {
        fakeUserRegistration.userProfile = invalidUserProfileOf(constraintViolations)
    }

    @DataTableType
    fun constraintViolations(violations: Map<String, String>): ConstraintViolation {
        return constraintViolationOf(violations.getValue("property"), Violation.valueOf(violations.getValue("violation")))
    }
}
