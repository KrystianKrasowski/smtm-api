package com.smtm.application.categories.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryDto {

    private final Long id;
    private final String name;
    private final String icon;

    public static CategoryDto of(String name, String icon) {
        return of(null, name, icon);
    }

    public static CategoryDto of(Long id, String name, String icon) {
        return new CategoryDto(id, name, icon);
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CategoryDto(@JsonProperty("id") Long id, @JsonProperty("name") String name, @JsonProperty("icon") String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }
}
