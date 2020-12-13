package com.smtm.application.security.users.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smtm.users.registration.EmailAddress;
import com.smtm.users.registration.EmailAddressKt;
import com.smtm.users.registration.PasswordKt;
import com.smtm.users.registration.UnencryptedPassword;

public class CredentialsDto {

    private final String email;
    private final String password;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CredentialsDto(@JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    public EmailAddress getEmail() {
        return EmailAddressKt.emailAddressOf(email);
    }

    public UnencryptedPassword getPassword() {
        return PasswordKt.unencryptedPasswordOf(password);
    }
}
