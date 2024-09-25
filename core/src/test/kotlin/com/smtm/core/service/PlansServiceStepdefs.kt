package com.smtm.core.service

import com.smtm.core.World
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Violation
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.core.domain.plans.PlansProblem
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import javax.money.MonetaryAmount

class PlansServiceStepdefs(private val world: World) {

    private val plansService get() = PlansService(world.categoriesRepository, world.plansRepository)

    private var entries: MutableList<Plan.Entry> = mutableListOf()
    private lateinit var planHeader: PlanHeader
    private lateinit var plan: Plan
    private lateinit var problem: PlansProblem

    @Given("plan is defined")
    fun `plan is defined`(planHeader: PlanHeader) {
        this.planHeader = planHeader
    }

    @Given("plan has category {string} with value {money}")
    fun `plan has category with value`(categoryId: String, value: MonetaryAmount) {
        entries.add(Plan.entry(EntityId.of(categoryId), value))
    }

    @When("user creates a plan")
    fun `user creates s plan`() {
        plansService.store(planHeader, entries)
            .onRight { plan = it }
            .onLeft { problem = it }
    }

    @Then("plan is not saved due to constraint violation")
    fun `plan is not saved due to constraint violation`(violation: Violation) {

    }
}
