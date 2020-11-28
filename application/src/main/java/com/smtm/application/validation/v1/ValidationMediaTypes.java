package com.smtm.application.validation.v1;

import org.springframework.http.MediaType;

public class ValidationMediaTypes {

    public static final String CONSTRAINT_VIOLATION_VALUE = "application/smtm.constraint-violations.v1+json";
    public static final MediaType CONSTRAINT_VIOLATION = MediaType.parseMediaType(CONSTRAINT_VIOLATION_VALUE);
}
