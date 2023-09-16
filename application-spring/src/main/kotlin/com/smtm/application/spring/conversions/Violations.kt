package com.smtm.application.spring.conversions

import com.smtm.application.domain.Violation
import com.smtm.application.v1.ApiProblemDto

object Violations {

    fun Violation.toDto(): ApiProblemDto.Violation =
        ApiProblemDto.Violation(
            path = path.toJsonPath(),
            code = code.name,
            message = code.name,
            parameters = parameters
        )
}
