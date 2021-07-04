package com.smtm.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ParameterTypesTest {

    private val parameterTypes = ParameterTypes()

    @Test
    fun `should parse violation message parameters`() {
        // when
        val parameters = parameterTypes.violationMessageParameters("email=john.doe@email.com;phone=123456789")

        // then
        assertThat(parameters).isEqualTo(mapOf(
            "email" to "john.doe@email.com",
            "phone" to "123456789"
        ))
    }

    @Test
    fun `should create constraint violation`() {
        // when
        val constraintViolation = parameterTypes.constraintViolation(
            mapOf(
                "property" to "email",
                "message" to "%email% is not a valid e-mail address",
                "parameters" to "email=john.doe#gmail.com"
            )
        )

        // then
        assertThat(constraintViolation).isEqualTo(constraintViolationOf(
            property = "email",
            message = messageOf(
                pattern = "%email% is not a valid e-mail address",
                parameters = mapOf(
                    "email" to "john.doe#gmail.com"
                )
            )
        ))
    }
}
