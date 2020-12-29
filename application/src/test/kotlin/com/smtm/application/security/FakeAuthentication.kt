package com.smtm.application.security;

import static com.smtm.security.token.TokenKt.tokenOf;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.smtm.application.security.v1.CredentialsDto;
import com.smtm.security.api.Authentication;
import com.smtm.security.authentication.TokenFactoryKt;
import com.smtm.security.registration.EmailAddress;
import com.smtm.security.registration.UnencryptedPassword;
import com.smtm.security.token.Token;
import com.smtm.security.token.TokenKt;

public class FakeAuthentication implements Authentication {

    private final String secret;
    private final Map<String, Token> validTokens;

    public FakeAuthentication(String secret) {
        this.secret = secret;
        this.validTokens = new HashMap<>();
    }

    @Nullable
    @Override
    public Token authenticate(@NotNull EmailAddress emailAddress, @NotNull UnencryptedPassword password) {
        return validTokens.get(createKey(emailAddress, password));
    }

    void setValidTokenFor(CredentialsDto credentials, String token) {
        validTokens.put(createKey(credentials), tokenOf(token, 1));
    }

    private String createKey(CredentialsDto credentials) {
        return createKey(credentials.getEmail(), credentials.getPassword());
    }

    private String createKey(EmailAddress email, UnencryptedPassword password) {
        return createKey(email.toString(), password.toString());
    }

    private String createKey(String email, String password) {
        return email + "." + password;
    }
}
