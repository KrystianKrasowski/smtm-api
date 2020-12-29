package com.smtm.application.security

import io.cucumber.java.en.Given

class AuthorizationStepdefs(private val fakeAuthorization: FakeAuthorization) {

    @Given("valid tokens are")
    fun validTokensAre(tokens: List<String>) {
        fakeAuthorization.validTokens.addAll(tokens)
    }
}
