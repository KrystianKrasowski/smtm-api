package com.smtm.application.security.v1;

import com.smtm.security.authentication.Token;

public class TokenDto {

    private final String value;

    public static TokenDto of(String token) {
        return new TokenDto(token);
    }

    public static TokenDto of(Token token) {
        return of(token.getValue());
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenDto tokenDto = (TokenDto) o;

        return value.equals(tokenDto.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    private TokenDto(String value) {
        this.value = value;
    }
}
