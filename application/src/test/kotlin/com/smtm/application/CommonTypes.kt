package com.smtm.application

import com.smtm.security.registration.ConstraintViolation
import com.smtm.security.registration.Violation
import com.smtm.security.registration.constraintViolationOf
import io.cucumber.java.DataTableType

class CommonTypes {

    @DataTableType
    fun constraintViolations(violations: Map<String, String>): ConstraintViolation {
        return constraintViolationOf(violations.getValue("property"), Violation.valueOf(violations.getValue("violation")))
    }
}
