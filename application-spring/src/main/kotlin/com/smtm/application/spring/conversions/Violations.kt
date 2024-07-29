package com.smtm.application.spring.conversions

import com.smtm.core.domain.Violation
import com.smtm.api.v1.ApiProblemDto

object Violations {

    fun Violation.toDto(): ApiProblemDto.Violation =
        ApiProblemDto.Violation(
            path = path.toJsonPath(),
            code = code.name,
            message = code.name,
            parameters = parameters
        )
}
