package com.smtm.application.security;

import static com.smtm.security.token.TokenKt.tokenOf;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.smtm.security.api.Authorization;
import com.smtm.security.token.Token;

public class FakeAuthorization implements Authorization {

    private final List<String> validTokens;

    public FakeAuthorization() {
        this.validTokens = new ArrayList<>();
    }

    @Nullable
    @Override
    public Token authorize(@NotNull String token) {
        if (validTokens.contains(token)) {
            return tokenOf(token, 1);
        }

        return null;
    }

    void addValidTokens(List<String> tokens) {
        this.validTokens.addAll(tokens);
    }
}
