package com.smtm.application.common.dto

import com.smtm.application.transactions.categories.v1.ConstraintViolation
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.http.ResponseEntity

data class ViolationsProblemDto(val violations: List<ConstraintViolation>)

fun List<ConstraintViolation>.toResponse400(title: String): ResponseEntity<Problem> = Problem.create()
    .withTitle(title)
    .withProperties(ViolationsProblemDto(this))
    .toResponse400()

fun Problem.toResponse400() = ResponseEntity.badRequest()
    .contentType(MediaTypes.HTTP_PROBLEM_DETAILS_JSON)
    .body(this)
