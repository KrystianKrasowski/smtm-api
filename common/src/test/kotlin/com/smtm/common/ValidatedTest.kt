package com.smtm.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ValidatedTest {

    @Test
    fun `should be valid`() {
        val result = validationSuccessOf("I'm a success")
            .map { 123 }
            .orElse { 456 }

        assertThat(result).isEqualTo(123)
    }

    @Test
    fun `should be invalid`() {
        val violation1 = constraintViolationOf(
            property = "name",
            message = messageOf(
                pattern = "Some message pattern"
            )
        )

        val violation2 = constraintViolationOf(
            property = "icon",
            message = messageOf(
                pattern = "Some other pattern"
            )
        )

        val result = validationFailureOf<Nothing>(listOf(violation1, violation2))
            .map { 12 }
            .map { 34 }
            .orElse { 56 }

        assertThat(result).isEqualTo(56)
    }
}
