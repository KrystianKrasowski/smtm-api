package com.smtm.application.security;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.smtm.security.api.Authorization;
import com.smtm.security.authentication.Token;
import com.smtm.security.authentication.TokenKt;

public class FakeAuthorization implements Authorization {

    private final String secret;

    private final List<String> validTokens;

    public FakeAuthorization(String secret) {
        this.secret = secret;
        this.validTokens = new ArrayList<>();
    }

    @Nullable
    @Override
    public Token authorize(@NotNull String token) {
        if (validTokens.contains(token)) {
            return TokenKt.tokenOf(token, secret);
        }

        return null;
    }

    void addValidTokens(List<String> tokens) {
        this.validTokens.addAll(tokens);
    }
}
