package com.smtm.application.security;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.smtm.security.api.Authorization;
import com.smtm.security.authentication.Token;
import com.smtm.security.authentication.TokenKt;

public class FakeAuthorization implements Authorization {

    private final Clock clock = Clock.fixed(Instant.parse("2020-10-26T00:00:00.00Z"), ZoneId.of("Europe/Warsaw"));
    private final String secret;

    public FakeAuthorization(String secret) {
        this.secret = secret;
    }

    @Nullable
    @Override
    public Token authorize(@NotNull String token) {
        return TokenKt.tokenOf(1L, Date.from(clock.instant().plusSeconds(900)), secret);
    }
}
