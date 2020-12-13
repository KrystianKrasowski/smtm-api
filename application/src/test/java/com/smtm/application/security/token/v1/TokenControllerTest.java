package com.smtm.application.security.token.v1;

import static com.smtm.security.registration.EmailAddressKt.emailAddressOf;
import static com.smtm.security.registration.PasswordKt.unencryptedPasswordOf;
import static com.smtm.security.token.TokenKt.tokenOf;
import static org.mockito.BDDMockito.given;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import com.smtm.application.assertions.SmtmAssertions;
import com.smtm.application.security.users.v1.CredentialsDto;
import com.smtm.security.api.TokenGenerator;

class TokenControllerTest {

    private TokenGenerator tokenGenerator;

    private TokenController tokenController;

    @BeforeEach
    void setUp() {
        tokenGenerator = Mockito.mock(TokenGenerator.class);
        tokenController = new TokenController(tokenGenerator);
    }

    @Test
    void shouldReturnTokenEntity() {
        // given
        given(tokenGenerator.generate(emailAddressOf("email"), unencryptedPasswordOf("password"))).willReturn(tokenOf("abcd"));

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
        given(tokenGenerator.generate(emailAddressOf("email"), unencryptedPasswordOf("password"))).willReturn(null);

        // when
        ResponseEntity<?> response = tokenController.createToken(CredentialsDto.of("email", "password"));

        // then
        SmtmAssertions.assertThat(response)
            .hasStatus(401);
    }
}
