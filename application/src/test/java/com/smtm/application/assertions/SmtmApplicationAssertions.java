package com.smtm.application.assertions;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public class SmtmApplicationAssertions {

    public static <T> ResponseEntityAssert<T> assertThat(ResponseEntity<T> responseEntity) {
        return new ResponseEntityAssert<>(responseEntity);
    }

    public static <T> EntityModelAssert<T> assertThat(EntityModel<T> entityModel) {
        return new EntityModelAssert<>(entityModel);
    }
}
