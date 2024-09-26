package com.smtm.application.spring.endpoints.exceptions

import com.smtm.api.MediaType
import com.smtm.api.v1.ApiProblemDto
import com.smtm.application.spring.conversions.Violations.toDto
import com.smtm.core.domain.plans.PlansProblem
import org.springframework.http.ResponseEntity

class PlansProblemException(private val plansProblem: PlansProblem) : Throwable() {

    fun toResponseEntity(): ResponseEntity<ApiProblemDto> =
        when (plansProblem) {
            is PlansProblem.Failure,
            is PlansProblem.CategoriesFetchingFailure -> ResponseEntity
                .internalServerError()
                .body(ApiProblemDto.Undefined())

            is PlansProblem.NotFound -> ResponseEntity
                .status(404)
                .header("Content-Type", MediaType.PROBLEM)
                .body(ApiProblemDto.UnknownResource())

            is PlansProblem.ValidationErrors -> ResponseEntity
                .status(422)
                .header("Content-Type", MediaType.PROBLEM)
                .body(ApiProblemDto.ConstraintViolations(plansProblem.violations.map { it.toDto() }))
        }
}
