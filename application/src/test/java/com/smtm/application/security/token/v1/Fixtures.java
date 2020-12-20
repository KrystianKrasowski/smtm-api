package com.smtm.application.security.token.v1;

import com.smtm.security.authentication.Token;
import com.smtm.security.authentication.TokenKt;

final class Fixtures {

    static Token tokenOf(String value) {
        return TokenKt.tokenOf(value, "aaa");
    }
}
