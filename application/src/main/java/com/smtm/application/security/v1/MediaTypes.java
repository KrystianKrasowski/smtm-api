package com.smtm.application.security.v1;

import org.springframework.http.MediaType;

class MediaTypes {

    static final String CREDENTIALS_VALUE = "application/smtm.credentials.v1+json";
    static final String USER_PROFILE_VALUE = "application/smtm.user-profile.v1+json";
    static final MediaType USER_PROFILE = MediaType.parseMediaType(USER_PROFILE_VALUE);
    static final String TOKEN_VALUE = "application/smtm.token.v1+json";
}
