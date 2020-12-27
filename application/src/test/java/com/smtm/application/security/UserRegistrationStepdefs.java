package com.smtm.application.security;

import static com.smtm.security.registration.ConstraintViolationKt.constraintViolationOf;
import java.util.List;
import java.util.Map;
import com.smtm.security.registration.ConstraintViolation;
import com.smtm.security.registration.UserProfile;
import com.smtm.security.registration.Violation;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;

public class UserRegistrationStepdefs {

    private final FakeUserRegistration fakeUserRegistration;

    public UserRegistrationStepdefs(FakeUserRegistration fakeUserRegistration) {
        this.fakeUserRegistration = fakeUserRegistration;
    }

    @Given("constraint violations are")
    public void constraintViolationsAre(List<ConstraintViolation> constraintViolations) {
        fakeUserRegistration.setUserProfile(new UserProfile.Invalid(constraintViolations));
    }

    @DataTableType
    public ConstraintViolation constraintViolations(Map<String, String> violations) {
        return constraintViolationOf(violations.get("property"), Violation.valueOf(violations.get("violation")));
    }
}
