package com.smtm.core.service

import assertk.assertThat
import assertk.assertions.containsAtLeast
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import com.smtm.core.World
import com.smtm.core.domain.Violation
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.core.domain.plans.PlansProblem
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class PlansServiceStepdefs(private val world: World) {

    private val plansService get() = PlansService(world.categoriesRepository, world.plansRepository)

    private var entries: List<Plan.Entry> = mutableListOf()
    private lateinit var planHeader: PlanHeader
    private lateinit var plan: Plan
    private lateinit var problem: PlansProblem

    @When("plan is defined")
    fun `plan is defined`(planHeader: PlanHeader) {
        this.planHeader = planHeader
    }

    @When("plan entries are defined")
    fun `plan entries are defined`(entries: List<Plan.Entry>) {
        this.entries = entries
    }

    @When("user creates a plan")
    fun `user creates s plan`() {
        plansService.create(planHeader, entries)
            .onRight { plan = it }
            .onLeft { problem = it }
    }

    @When("user updates a plan")
    fun `user updates a plan`() {
        plansService.update(planHeader, entries)
            .onRight { plan = it }
            .onLeft { problem = it }
    }

    @Then("plan is not saved due to constraint violations")
    fun `plan is not saved due to constraint violations`(violation: List<Violation>) {
        assertThat(problem)
            .isNotNull()
            .isInstanceOf(PlansProblem.ValidationErrors::class)
            .containsAtLeast(*violation.toTypedArray())
    }
}
