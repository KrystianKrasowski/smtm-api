package com.smtm.core.spi

import com.smtm.core.World
import io.cucumber.java.en.Then
import kotlin.test.assertTrue

class PlansRepositoryStepdefs(private val world: World) {

    @Then("plan {string} is saved successfully")
    fun `plan is saved successfully`(planId: String) {
        assertTrue(world.plansRepository.hasPlan(planId))
    }
}
