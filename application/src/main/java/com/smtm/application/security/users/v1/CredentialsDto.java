package com.smtm.application.security.users.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smtm.security.registration.EmailAddress;
import com.smtm.security.registration.EmailAddressKt;
import com.smtm.security.registration.UnencryptedPassword;
import com.smtm.security.registration.UnencryptedPasswordKt;

public class CredentialsDto {

    private final String email;
    private final String password;

    public static CredentialsDto of(String email, String password) {
        return new CredentialsDto(email, password);
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CredentialsDto(@JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    public EmailAddress getEmail() {
        return EmailAddressKt.emailAddressOf(email);
    }

    public UnencryptedPassword getPassword() {
        return UnencryptedPasswordKt.unencryptedPasswordOf(password);
    }
}
