package com.smtm.application.users.v1;

import static com.smtm.application.assertions.SmtmApplicationAssertions.assertThat;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import com.smtm.users.registration.UserProfileKt;

class UsersControllerTest {

    private FakeUserRegistration userRegistration;
    private UsersController controller;

    @BeforeEach
    void setUp() {
        userRegistration = new FakeUserRegistration();
        controller = new UsersController(userRegistration);
    }

    @Test
    void shouldReturnHttpStatus201() {
        // when
        ResponseEntity<?> response = controller.registerUser();

        // then
        assertThat(response).hasHttpStatus(201);
    }

    @Test
    void shouldReturnHttpContentType() {
        // when
        ResponseEntity<?> response = controller.registerUser();

        // then
        assertThat(response).hasContentType("application/smtm.user-profile.v1+json");
    }

    @Test
    void shouldReturnUserProfileResource() {
        // given
        userRegistration.returns(UserProfileKt.validUserProfileOf(1, "john.doe@gmail.com"));

        // when
        ResponseEntity<?> response = controller.registerUser();

        // then
        assertThat(response).hasLink("/users/1", "self");
        assertThat(response).hasEntityModel(UserProfileDto.of(1, "john.doe@gmail.com"));
    }

    @Test
    void shouldReturnConstraintViolationsResource() {
        // given
        HashMap<String, String> violations = new HashMap<>();
        violations.put("email", "e-mail is already registered");
        violations.put("password", "password does not meet the security policy");
        userRegistration.returns(UserProfileKt.invalidUserProfileOf(violations));

        // when
        ResponseEntity<?> response = controller.registerUser();

        // then
        assertThat(response).hasHttpStatus(400);
        assertThat(response).hasContentType("application/smtm.constraint-violations.v1+json");
        assertThat(response).hasConstraintViolation("email", "e-mail is already registered");
        assertThat(response).hasConstraintViolation("password", "password does not meet the security policy");
    }
}
