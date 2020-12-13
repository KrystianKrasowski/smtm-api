package com.smtm.application.security.users.v1;

import org.springframework.http.MediaType;

class UserMediaTypes {

    static final String NEW_USER_VALUE = "application/smtm.credentials.v1+json";
    static final String USER_PROFILE_VALUE = "application/smtm.user-profile.v1+json";

    static final MediaType USER_PROFILE = MediaType.parseMediaType(USER_PROFILE_VALUE);
}
