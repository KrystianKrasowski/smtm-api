package com.smtm.application.assertions;

import java.util.Optional;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.Nullable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import com.smtm.application.validation.v1.ConstraintViolationsDto;

@SuppressWarnings("unchecked")
public class ResponseEntityAssert<T> extends AbstractAssert<ResponseEntityAssert<T>, ResponseEntity<T>> {

    ResponseEntityAssert(ResponseEntity<T> tResponseEntity) {
        super(tResponseEntity, ResponseEntityAssert.class);
    }

    public ResponseEntityAssert<T> hasHttpStatus(int status) {
        isNotNull();

        if (actual.getStatusCodeValue() != status) {
            failWithMessage("Expected HTTP status to be <%s>, but was <%s>", status, actual.getStatusCodeValue());
        }

        return myself;
    }

    public ResponseEntityAssert<T> hasContentType(String contentType) {
        isNotNull();

        String actualContentType = Optional.ofNullable(actual)
            .map(HttpEntity::getHeaders)
            .map(HttpHeaders::getContentType)
            .map(MimeType::toString)
            .orElse(null);

        if (!contentType.equals(actualContentType)) {
            failWithMessage("Expected Content-Type to be <%s>, but was <%s>", contentType, actualContentType);
        }

        return myself;
    }

    public ResponseEntityAssert<T> hasLink(String href, String relation) {
        isNotNull();
        isEntityModel();
        SmtmApplicationAssertions.assertThat(extractEntityModel()).hasLink(href, relation);
        return myself;
    }

    public ResponseEntityAssert<T> hasEntityModel(Object model) {
        isNotNull();
        isEntityModel();
        SmtmApplicationAssertions.assertThat(extractEntityModel()).hasContent(model);
        return myself;
    }

    public ResponseEntityAssert<T> isEntityModel() {
        T body = actual.getBody();
        Assertions.assertThat(body).isInstanceOf(EntityModel.class);
        return myself;
    }

    public ResponseEntityAssert<T> hasConstraintViolation(String key, String violationMessage) {
        isNotNull();
        ConstraintViolationsDto violationsDto = extractConstraintViolations();
        Assertions.assertThat(violationsDto.getViolations()).containsEntry(key, violationMessage);
        return myself;
    }

    @Nullable
    private EntityModel<Object> extractEntityModel() {
        return (EntityModel<Object>) actual.getBody();
    }

    private ConstraintViolationsDto extractConstraintViolations() {
        return (ConstraintViolationsDto) actual.getBody();
    }
}
