package com.smtm.security.spi

import com.smtm.security.World
import com.smtm.security.assertions.assertThat
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then

class RefreshTokenRepositoryStepdefs(private val world: World) {

    @Given("refresh token repository contains")
    fun `refresh token repository contains`(record: FakeRefreshTokensRepository.Record) {
        world.refreshTokenRepository.addRecord(record)
    }

    @Then("refresh token {string} is stored for subject {int}")
    fun `refresh token xxx is stored for subject xxx`(tokenId: String, subject: Long) {
        assertThat(world.refreshTokenRepository).hasEntry(subject, tokenId)
    }
}
