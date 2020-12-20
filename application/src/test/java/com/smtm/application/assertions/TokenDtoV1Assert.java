package com.smtm.application.assertions;

import org.assertj.core.api.AbstractAssert;
import com.smtm.application.security.token.v1.TokenDto;

public class TokenDtoV1Assert extends AbstractAssert<TokenDtoV1Assert, TokenDto> {

    static TokenDtoV1Assert assertThat(TokenDto tokenDto) {
        return new TokenDtoV1Assert(tokenDto);
    }

    public TokenDtoV1Assert hasValue(String value) {
        isNotNull();

        if (!value.equals(actual.toString())) {
            failWithMessage("Expected token's value to be <%s>, but was <%s>", value, actual);
        }

        return myself;
    }

    private TokenDtoV1Assert(TokenDto tokenDto) {
        super(tokenDto, TokenDtoV1Assert.class);
    }
}
