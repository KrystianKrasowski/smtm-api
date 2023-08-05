package com.smtm.application.service

import com.smtm.application.World
import com.smtm.application.domain.Violation
import com.smtm.application.domain.plans.NewPlan
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlansProblem
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat

class PlansServiceStepdefs(private val world: World) {

    private var plan: Plan? = null
    private var problem: PlansProblem? = null

    @When("user saves a new plan")
    fun `users saves a new plan`(plan: NewPlan) {
        val service = PlansService(world.categoriesRepository, world.plansRepository)
        service.create(plan, world.ownerId)
            .onRight { this.plan = it }
            .onLeft { this.problem = it }
    }

    @Then("plan is not saved due to constraint violation")
    fun `plan is not saved due to constraint violation`(violation: Violation) {
        assertThat(problem).isInstanceOf(PlansProblem.Violations::class.java)
        assertThat(problem?.asViolations()?.violations).contains(violation)
    }

    @Then("plan is saved")
    fun `plan is saved`() {
        assertThat(plan).isNotNull
    }
}

private fun PlansProblem.asViolations() =
    this as PlansProblem.Violations