package com.smtm.application.validation.v1;

import java.util.Map;
import com.smtm.users.registration.UserProfile;

public class ConstraintViolationsDto {

    private final Map<String, String> violations;

    public static ConstraintViolationsDto of(UserProfile.Invalid invalidUserProfile) {
        return of(invalidUserProfile.getViolations());
    }

    public static ConstraintViolationsDto of(Map<String, String> violations) {
        return new ConstraintViolationsDto(violations);
    }

    private ConstraintViolationsDto(Map<String, String> violations) {
        this.violations = violations;
    }

    public Map<String, String> getViolations() {
        return violations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstraintViolationsDto that = (ConstraintViolationsDto) o;

        return violations.equals(that.violations);
    }

    @Override
    public int hashCode() {
        return violations.hashCode();
    }
}
