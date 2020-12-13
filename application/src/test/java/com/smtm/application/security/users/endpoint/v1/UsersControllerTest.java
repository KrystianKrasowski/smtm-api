package com.smtm.application.security.users.endpoint.v1;

import static com.smtm.security.registration.ConstraintViolationKt.constraintViolationOf;
import static com.smtm.security.registration.EmailAddressKt.*;
import static com.smtm.security.registration.PasswordKt.unencryptedPasswordOf;
import static com.smtm.security.registration.UserProfileKt.invalidUserProfileOf;
import static com.smtm.security.registration.UserProfileKt.validUserProfileOf;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.smtm.application.security.users.v1.UsersController;
import com.smtm.security.api.UserRegistration;
import com.smtm.security.registration.*;

@WebMvcTest(UsersController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRegistration userRegistration;

    @Test
    void shouldReturnUserProfile() throws Exception {
        // given
        EmailAddress email = emailAddressOf("john.doe@gmail.com");
        UnencryptedPassword password = unencryptedPasswordOf("S3cr3t!");
        when(userRegistration.register(email, password)).thenReturn(validUserProfileOf(1, email));

        // when
        ResultActions result = mockMvc.perform(post("/users")
            .contentType("application/smtm.new-user.v1+json")
            .accept("application/smtm.user-profile.v1+json")
            .content("{ \"email\": \"john.doe@gmail.com\", \"password\": \"S3cr3t!\" }"));

        // then
        result.andExpect(status().isCreated())
            .andExpect(content().contentType("application/smtm.user-profile.v1+json"))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.email", is("john.doe@gmail.com")))
            .andExpect(jsonPath("$.links[0].rel", is("self")))
            .andExpect(jsonPath("$.links[0].href", is("http://localhost/users/1")));
    }

    @Test
    void shouldReturnConstraintViolations() throws Exception {
        // given
        ArrayList<ConstraintViolation> constraintViolations = new ArrayList<>();
        constraintViolations.add(constraintViolationOf("email", Violation.NonUnique));
        constraintViolations.add(constraintViolationOf("email", Violation.NotAnEmailAddress));
        constraintViolations.add(constraintViolationOf("password", Violation.NotEnoughDigits));
        constraintViolations.add(constraintViolationOf("password", Violation.NotEnoughLength));
        constraintViolations.add(constraintViolationOf("password", Violation.NotEnoughSpecialChars));
        constraintViolations.add(constraintViolationOf("password", Violation.NotEnoughUppercaseLetters));
        EmailAddress email = emailAddressOf("john.doe@gmail.com");
        UnencryptedPassword password = unencryptedPasswordOf("S3cr3t!");
        when(userRegistration.register(email, password)).thenReturn(invalidUserProfileOf(constraintViolations));

        // when
        ResultActions result = mockMvc.perform(post("/users")
            .contentType("application/smtm.new-user.v1+json")
            .accept("application/smtm.user-profile.v1+json")
            .content("{ \"email\": \"john.doe@gmail.com\", \"password\": \"S3cr3t!\" }"));

        // then
        result.andExpect(status().isBadRequest())
            .andExpect(content().contentType("application/smtm.constraint-violations.v1+json"))
            .andExpect(jsonPath("$.violations[0].property", is("email")))
            .andExpect(jsonPath("$.violations[0].violationMessage", is("value is not unique")))
            .andExpect(jsonPath("$.violations[1].property", is("email")))
            .andExpect(jsonPath("$.violations[1].violationMessage", is("value is not an email address")))
            .andExpect(jsonPath("$.violations[2].property", is("password")))
            .andExpect(jsonPath("$.violations[2].violationMessage", is("not enough digits")))
            .andExpect(jsonPath("$.violations[3].property", is("password")))
            .andExpect(jsonPath("$.violations[3].violationMessage", is("not enough length")))
            .andExpect(jsonPath("$.violations[4].property", is("password")))
            .andExpect(jsonPath("$.violations[4].violationMessage", is("not enough special chars")))
            .andExpect(jsonPath("$.violations[5].property", is("password")))
            .andExpect(jsonPath("$.violations[5].violationMessage", is("not enough uppercase letters")));
    }
}
