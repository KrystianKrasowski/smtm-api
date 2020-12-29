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

    @Given("token {string} is produced by credentials")
    public void tokenIsProducedByCredentials(String token, CredentialsDto credentials) {
        fakeAuthentication.setValidTokenFor(credentials, token);
    }

    @DataTableType
    public CredentialsDto credentialsDto(Map<String, String> credentials) {
        return CredentialsDto.of(credentials.get("email"), credentials.get("password"));
    }
}
