package com.smtm.application.categories.v1;

import org.springframework.http.MediaType;

final class MediaTypes {

    static final String CATEGORY_VALUE = "application/smtm.category.v1+json";
    static final MediaType CATEGORY = MediaType.parseMediaType(CATEGORY_VALUE);
}
