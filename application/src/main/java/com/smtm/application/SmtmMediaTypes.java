package com.smtm.application;

import org.springframework.http.MediaType;

public class SmtmMediaTypes {

    public static class Security {

        public static class V1 {

            public static final String CREDENTIALS_VALUE = "application/smtm.credentials.v1+json";
            public static final String USER_PROFILE_VALUE = "application/smtm.user-profile.v1+json";
            public static final String TOKEN_VALUE = "application/smtm.token.v1+json";
            public static final MediaType USER_PROFILE = MediaType.parseMediaType(USER_PROFILE_VALUE);
        }
    }

    public static class Validation {

        public static class V1 {

            public static final String CONSTRAINT_VIOLATION_VALUE = "application/smtm.constraint-violations.v1+json";
            public static final MediaType CONSTRAINT_VIOLATION = MediaType.parseMediaType(CONSTRAINT_VIOLATION_VALUE);
        }
    }
}
