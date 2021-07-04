package com.smtm.security.spi

import com.smtm.security.World
import io.cucumber.java.en.Given
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class ClockStepdefs(private val world: World) {

    @Given("current time is {instant}")
    fun `current time is xxx`(time: Instant) {
        world.clock = Clock.fixed(time, ZoneId.of("UTC"))
    }
}
