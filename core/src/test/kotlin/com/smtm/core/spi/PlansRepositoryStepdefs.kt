package com.smtm.core.spi

import arrow.core.getOrElse
import com.smtm.core.World
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import kotlin.test.assertTrue

class PlansRepositoryStepdefs(private val world: World) {

    @Given("user plans are")
    fun `user plans are`(planHeader: List<PlanHeader>) {
        planHeader
            .map { createPlan(it) }
            .let { world.plansRepository.plans = it.toMutableList() }
    }

    @Then("plan {string} is saved successfully")
    fun `plan is saved successfully`(planId: String) {
        assertTrue(world.plansRepository.hasPlan(planId))
    }

    private fun createPlan(header: PlanHeader): Plan =
        Plan.validated(header, emptyList(), world.categoriesRepository.categoryList)
            .getOrElse { error("given plan should be valid") }
}
