package com.smtm.common

import io.cucumber.java.DataTableType
import io.cucumber.java.ParameterType

class ParameterTypes {

    @ParameterType("(([A-Za-z0-9-_ .,]+=[A-Za-z0-9-_ .,@]+(,)?)+)")
    fun violationMessageParameters(input: String) = input
        .split(",")
        .map { it.trim() }
        .map { parameter ->
            parameter
                .split("=")
                .let { Pair(it[0], it[1]) }
        }
        .toMap()

    @DataTableType
    fun constraintViolation(input: Map<String, String>) = constraintViolationOf(
        property = input.getValue("property"),
        message = messageOf(
            pattern = input.getValue("message"),
            parameters = violationMessageParameters(input.getValue("parameters"))
        )
    )
}
