package com.smtm.application.users.endpoint.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smtm.users.registration.PasswordKt;
import com.smtm.users.registration.UnsecuredPassword;

public class NewUserDto {

    private final String email;
    private final String password;

    public static NewUserDto of(String email, String password) {
        return new NewUserDto(email, password);
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public NewUserDto(@JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UnsecuredPassword getPasswordAsUnsecured() {
        return PasswordKt.unsecuredPasswordOf(password);
    }
}
