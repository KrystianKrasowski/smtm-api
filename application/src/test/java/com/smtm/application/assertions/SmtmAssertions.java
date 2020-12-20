package com.smtm.application.assertions;

import org.springframework.http.ResponseEntity;
import com.smtm.application.security.token.v1.TokenDto;

public final class SmtmAssertions {

    public static ResponseEntityAssert assertThat(ResponseEntity<?> responseEntity) {
        return ResponseEntityAssert.assertThat(responseEntity);
    }

    public static TokenDtoV1Assert assertThat(TokenDto tokenDto) {
        return TokenDtoV1Assert.assertThat(tokenDto);
    }

    private SmtmAssertions() {
    }
}
