package com.smtm.application.spring.exceptions

import com.smtm.application.v1.ApiProblemDto
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ResourceExceptionsHandler {

    @ExceptionHandler(ConstraintViolationsException::class)
    fun handleConstraintViolationsProblem(exception: ConstraintViolationsException): ResponseEntity<ApiProblemDto.ConstraintViolations> {
        return ResponseEntity
            .unprocessableEntity()
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(exception.dto)
    }

    @ExceptionHandler(ApiProblemException::class)
    fun handleApiProblemException(exception: ApiProblemException): ResponseEntity<ApiProblemDto.Undefined> {
        return ResponseEntity
            .internalServerError()
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(exception.dto)
    }
}