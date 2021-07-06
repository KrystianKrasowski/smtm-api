package com.smtm.application.security

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GuidGeneratorAdapterTest {

    private val generator = GuidGeneratorAdapter()

    @Test
    fun shouldGenerateGuid() {
        // when
        val guid = generator.generate()

        // then
        assertThat(guid).matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")
    }
}
