package com.smtm.application.validation.v1;

import java.util.HashMap;
import java.util.Map;
import com.smtm.users.registration.Violation;

public class ConstraintViolationDto {

    private static final Map<Violation, String> DICTIONARY = new HashMap<>();

    static {
        DICTIONARY.put(Violation.NonUnique, "value is not unique");
        DICTIONARY.put(Violation.TooWeak, "value is too weak");
    }

    private final String property;
    private final String violationMessage;

    public static ConstraintViolationDto of(String property, Violation violation) {
        return of(property, DICTIONARY.get(violation));
    }

    public static ConstraintViolationDto of(String property, String message) {
        return new ConstraintViolationDto(property, message);
    }

    private ConstraintViolationDto(String property, String violationMessage) {
        this.property = property;
        this.violationMessage = violationMessage;
    }

    public String getProperty() {
        return property;
    }

    public String getViolationMessage() {
        return violationMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstraintViolationDto that = (ConstraintViolationDto) o;

        if (!property.equals(that.property)) return false;
        return violationMessage.equals(that.violationMessage);
    }

    @Override
    public int hashCode() {
        int result = property.hashCode();
        result = 31 * result + violationMessage.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ConstraintViolationDto{" +
            "property='" + property + '\'' +
            ", violationMessage='" + violationMessage + '\'' +
            '}';
    }
}
