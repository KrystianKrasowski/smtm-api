package com.smtm.application.domain.categories

import com.smtm.application.World
import com.smtm.application.domain.Violation
import com.smtm.application.service.CategoriesService
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions

class CategoriesServiceSaveStepdefs(private val world: World) {

    private val service get() = CategoriesService(world.categoriesRepository)
    private var addedCategory: Category? = null
    private var problem: CategoriesProblem? = null

    @When("user saves category")
    fun `user saves category`(category: Category) {
        category.copy(status = Category.Status.NEW)
            .let { service.save(it, world.ownerId) }
            .onRight { this.addedCategory = it }
            .onLeft { this.problem = it }
    }

    @Then("constraint violations set contains")
    fun `constraint violations set contains`(violation: Violation) {
        Assertions.assertThat(this.problem).isInstanceOf(CategoriesProblem.Violations::class.java)
        val problem = this.problem as CategoriesProblem.Violations
        Assertions.assertThat(problem.violations).contains(violation)
    }
}

