package com.smtm.security.spi

import com.smtm.security.World
import io.cucumber.java.en.Given

class GuidGeneratorStepdefs(private val world: World) {

    @Given("next GUID is {string}")
    fun `next guid is`(guid: String) {
        world.guidGenerator.guid = guid
    }
}
