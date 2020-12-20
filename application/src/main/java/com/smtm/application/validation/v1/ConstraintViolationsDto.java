package com.smtm.application.validation.v1;

import java.util.List;
import java.util.stream.Collectors;
import com.smtm.security.registration.UserProfile;

public class ConstraintViolationsDto {

    private final List<ConstraintViolationDto> violations;

    public static ConstraintViolationsDto of(UserProfile.Invalid invalidUserProfile) {
        List<ConstraintViolationDto> violations = invalidUserProfile.getViolations()
            .stream()
            .map(violation -> ConstraintViolationDto.of(violation.getKey(), violation.getViolation()))
            .collect(Collectors.toList());
        return of(violations);
    }

    public static ConstraintViolationsDto of(List<ConstraintViolationDto> violations) {
        return new ConstraintViolationsDto(violations);
    }

    private ConstraintViolationsDto(List<ConstraintViolationDto> violations) {
        this.violations = violations;
    }

    public List<ConstraintViolationDto> getViolations() {
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
