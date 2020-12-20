package com.smtm.application.assertions;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.smtm.application.security.token.v1.TokenDto;

public class ResponseEntityAssert extends AbstractAssert<ResponseEntityAssert, ResponseEntity<?>> {

    static ResponseEntityAssert assertThat(ResponseEntity<?> responseEntity) {
        return new ResponseEntityAssert(responseEntity);
    }

    public ResponseEntityAssert hasStatus(int statusCode) {
        isNotNull();
        Assertions.assertThat(actual.getStatusCodeValue()).isEqualTo(statusCode);
        return myself;
    }

    public TokenDtoV1Assert hasBoundedTokenV1() {
        isNotNull();
        Assertions.assertThat(actual.getBody()).isInstanceOf(TokenDto.class);
        return SmtmAssertions.assertThat((TokenDto) actual.getBody());
    }

    private ResponseEntityAssert(ResponseEntity<?> responseEntity) {
        super(responseEntity, ResponseEntityAssert.class);
    }
}
