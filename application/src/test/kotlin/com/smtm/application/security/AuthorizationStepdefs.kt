package com.smtm.application.security;

import java.util.List;
import io.cucumber.java.en.Given;

public class AuthorizationStepdefs {

    private final FakeAuthorization fakeAuthorization;

    public AuthorizationStepdefs(FakeAuthorization fakeAuthorization) {
        this.fakeAuthorization = fakeAuthorization;
    }

    @Given("valid tokens are")
    public void validTokensAre(List<String> tokens) {
        fakeAuthorization.addValidTokens(tokens);
    }
}
