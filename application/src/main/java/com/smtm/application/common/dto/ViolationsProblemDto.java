package com.smtm.application.common.dto;

import java.util.List;
import com.smtm.security.registration.ConstraintViolation;

public class ViolationsProblemDto {

    private final List<ConstraintViolation> violations;

    public ViolationsProblemDto(List<ConstraintViolation> violations) {
        this.violations = violations;
    }

    public List<ConstraintViolation> getViolations() {
        return violations;
    }
}
