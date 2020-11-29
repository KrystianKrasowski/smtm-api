package com.smtm.application.users.endpoint.v1;

import static com.smtm.users.registration.ConstraintViolationKt.constraintViolationOf;
import static com.smtm.users.registration.PasswordKt.unencryptedPasswordOf;
import static com.smtm.users.registration.UserProfileKt.invalidUserProfileOf;
import static com.smtm.users.registration.UserProfileKt.validUserProfileOf;
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
import com.smtm.users.api.UserRegistration;
import com.smtm.users.registration.ConstraintViolation;
import com.smtm.users.registration.Violation;

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
        when(userRegistration.register("john.doe@gmail.com", unencryptedPasswordOf("S3cr3t!"))).thenReturn(validUserProfileOf(1, "john.doe@gmail.com"));

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
        constraintViolations.add(constraintViolationOf("password", Violation.TooWeak));
        when(userRegistration.register("john.doe@gmail.com", unencryptedPasswordOf("S3cr3t!"))).thenReturn(invalidUserProfileOf(constraintViolations));

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
            .andExpect(jsonPath("$.violations[1].property", is("password")))
            .andExpect(jsonPath("$.violations[1].violationMessage", is("value is too weak")));
    }
}
