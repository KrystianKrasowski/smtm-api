package com.smtm.application.spring.endpoints.exceptions

import com.smtm.api.MediaType
import com.smtm.api.v1.ApiProblemDto
import com.smtm.application.spring.conversions.Violations.toDto
import com.smtm.core.domain.categories.CategoriesProblem
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CategoriesProblemException(private val problem: CategoriesProblem) : Throwable() {

    fun toResponseEntity(): ResponseEntity<ApiProblemDto> =
        when (problem) {
            is CategoriesProblem.ValidationErrors -> ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .header("Content-Type", MediaType.PROBLEM)
                .body(ApiProblemDto.ConstraintViolations(problem.violations.map { it.toDto() }))

            is CategoriesProblem.Unknown -> ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("Content-Type", MediaType.PROBLEM)
                .body(ApiProblemDto.UnknownResource())

            is CategoriesProblem.Failure -> ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Content-Type", MediaType.PROBLEM)
                .body(ApiProblemDto.Undefined())
        }
}
