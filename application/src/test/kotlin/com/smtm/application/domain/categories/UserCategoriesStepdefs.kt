package com.smtm.application.domain.categories

import com.smtm.application.World
import com.smtm.application.domain.Violation
import com.smtm.application.service.CategoriesService
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions

class UserCategoriesStepdefs(private val world: World) {

    private val service get() = CategoriesService(world.categoriesRepository)

    private var addedCategory: Category? = null
    private var problem: CategoriesProblem? = null

    @When("user creates category")
    fun `user creates category`(category: Category) {
        service
            .create(category, world.ownerId)
            .onLeft { this.problem = it }
            .onRight { this.addedCategory = it }
    }

    @Then("user categories contains")
    fun `user categories contains`(category: Category) {
        val categories = service.getAll(world.ownerId).getOrNull()
        Assertions.assertThat(categories).contains(category)
    }

    @Then("constraint violations set contains")
    fun `constraint violations set contains`(violation: Violation) {
        Assertions.assertThat(this.problem).isInstanceOf(CategoriesProblem.Violations::class.java)
        val problem = this.problem as CategoriesProblem.Violations
        Assertions.assertThat(problem.violations).contains(violation)
    }
}

