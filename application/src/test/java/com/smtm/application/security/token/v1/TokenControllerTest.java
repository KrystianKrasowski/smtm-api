package com.smtm.application.security.token.v1;

import static com.smtm.application.security.token.v1.Fixtures.*;
import static com.smtm.security.registration.EmailAddressKt.emailAddressOf;
import static com.smtm.security.registration.UnencryptedPasswordKt.unencryptedPasswordOf;
import static org.mockito.BDDMockito.given;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import com.smtm.application.assertions.SmtmAssertions;
import com.smtm.application.security.users.v1.CredentialsDto;
import com.smtm.security.api.Authentication;

class TokenControllerTest {

    private Authentication authentication;

    private TokenController tokenController;

    @BeforeEach
    void setUp() {
        authentication = Mockito.mock(Authentication.class);
        tokenController = new TokenController(authentication);
    }

    @Test
    void shouldReturnTokenEntity() {
        // given
        given(authentication.authenticate(emailAddressOf("email"), unencryptedPasswordOf("password"))).willReturn(tokenOf("abcd"));

        // when
        ResponseEntity<?> response = tokenController.createToken(CredentialsDto.of("email", "password"));

        // then
        SmtmAssertions.assertThat(response)
            .hasStatus(200)
            .hasBoundedTokenV1()
            .hasValue("abcd");
    }

    @Test
    void shouldReturnUnauthorizedStatusCode() {
        // given
        given(authentication.authenticate(emailAddressOf("email"), unencryptedPasswordOf("password"))).willReturn(null);

        // when
        ResponseEntity<?> response = tokenController.createToken(CredentialsDto.of("email", "password"));

        // then
        SmtmAssertions.assertThat(response)
            .hasStatus(401);
    }
}
