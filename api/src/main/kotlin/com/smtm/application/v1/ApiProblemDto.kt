package com.smtm.application.v1

import com.fasterxml.jackson.annotation.JsonProperty

sealed class ApiProblemDto(
    @JsonProperty("type") val type: String,
    @JsonProperty("title") val title: String
) {

    class Undefined : ApiProblemDto("https://api.smtm.com/problems/undefined", "Some undefined problem occured")

    data class ConstraintViolations(
        val violations: Iterable<Violation>
    ) : ApiProblemDto("https://api.smtm.com/problems/constraint-violations", "Provided resource is not valid")

    data class Violation(
        @JsonProperty("path") val path: String,
        @JsonProperty("message") val message: String,
        @JsonProperty("code") val code: String,
        @JsonProperty("parameters") val parameters: Map<String, String>
    )
}