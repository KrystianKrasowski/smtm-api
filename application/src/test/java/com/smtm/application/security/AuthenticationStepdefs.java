package com.smtm.application.security;

import java.util.Map;
import com.smtm.application.security.v1.CredentialsDto;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;

public class AuthenticationStepdefs {

    private final FakeAuthentication fakeAuthentication;

    public AuthenticationStepdefs(FakeAuthentication fakeAuthentication) {
        this.fakeAuthentication = fakeAuthentication;
    }

    @Given("credentials produces a valid token")
    public void credentialsProducesAValidToken(CredentialsDto credentialsDto) {
        fakeAuthentication.setValidTokenFor(credentialsDto);
    }

    @DataTableType
    public CredentialsDto credentialsDto(Map<String, String> credentials) {
        return CredentialsDto.of(credentials.get("email"), credentials.get("password"));
    }
}
