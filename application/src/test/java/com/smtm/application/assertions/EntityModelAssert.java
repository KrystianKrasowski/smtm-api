package com.smtm.application.assertions;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class EntityModelAssert<T> extends AbstractAssert<EntityModelAssert<T>, EntityModel<T>> {

    EntityModelAssert(EntityModel<T> entityModel) {
        super(entityModel, EntityModelAssert.class);
    }

    public EntityModelAssert<T> hasLink(String href, String relation) {
        isNotNull();

        Link expected = Link.of(href, relation).withSelfRel();
        return actual.getLinks()
            .stream()
            .filter(it -> href.equals(it.getHref()) && relation.equals(it.getRel().toString()))
            .findFirst()
            .map(it -> myself)
            .orElseGet(() -> {
                failWithMessage("Expected entity to contain link <%s> among <%s>", expected, actual.getLinks());
                return myself;
            });
    }

    public EntityModelAssert<T> hasContent(T content) {
        Assertions.assertThat(actual.getContent()).isEqualTo(content);
        return myself;
    }
}
