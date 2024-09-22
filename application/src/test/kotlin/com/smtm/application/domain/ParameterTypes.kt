package com.smtm.application.domain

import io.cucumber.java.DataTableType

class ParameterTypes {

    @DataTableType
    fun constraintViolation(entry: Map<String, String>): Violation {
        return Violation(
            path = violationPathOf(entry.getValue("path")),
            code = Violation.Code.valueOf(entry.getValue("code"))
        )
    }
}