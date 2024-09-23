package com.smtm.core.domain

import io.cucumber.java.DataTableType
import io.cucumber.java.ParameterType
import javax.money.MonetaryAmount
import org.javamoney.moneta.Money

class ParameterTypes {

    @DataTableType
    fun constraintViolation(entry: Map<String, String>): Violation {
        return Violation(
            path = Violation.path(entry.getValue("path")),
            code = Violation.Code.valueOf(entry.getValue("code")),
            parameters = entry.extractParameters().toMap()
        )
    }

    @ParameterType("([A-Z]{3} \\d+(\\.\\d{1,2})*)")
    fun money(input: String?): MonetaryAmount {
        return Money.parse(input)
    }

    private fun Map<String, String>.extractParameters() = listOfNotNull(
        extractIllegalCharacters()
    )

    private fun Map<String, String>.extractIllegalCharacters() = get("illegal characters")
        ?.let { Pair("illegal-characters", it) }
}
