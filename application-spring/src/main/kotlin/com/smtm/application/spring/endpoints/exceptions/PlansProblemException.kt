package com.smtm.application.spring.endpoints.exceptions

import com.smtm.api.MediaType
import com.smtm.api.v1.ApiProblemDto
import com.smtm.core.domain.plans.PlansProblem
import org.springframework.http.ResponseEntity

class PlansProblemException(private val plansProblem: PlansProblem) : Throwable() {

    fun toResponseEntity(): ResponseEntity<ApiProblemDto> =
        when (plansProblem) {
            is PlansProblem.Failure -> ResponseEntity
                .internalServerError()
                .body(ApiProblemDto.Undefined())

            is PlansProblem.NotFound -> ResponseEntity
                .status(404)
                .header("Content-Type", MediaType.PROBLEM)
                .body(ApiProblemDto.UnknownResource())
        }
}
