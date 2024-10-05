package com.smtm.core.service

import assertk.assertThat
import assertk.assertions.containsAtLeast
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import com.smtm.core.World
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Violation
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.domain.categories.Category
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class CategoriesServiceStepdefs(private val world: World) {

    private val categoriesService get() = CategoriesService(world.categoriesRepository)

    private var problem: CategoriesProblem? = null

    @When("user creates new category")
    fun `user creates new category`(category: Category) {
        categoriesService.create(category)
            .onLeft { problem = it }
    }

    @When("user updates the category")
    fun `user updates the category`(category: Category) {
        categoriesService.update(category)
            .onLeft { problem = it }
    }

    @When("user deletes category of id {string}")
    fun `user deletes category of id N`(id: String) {
        categoriesService.delete(EntityId.of(id))
            .onLeft { problem = it }
    }

    @Then("category is not saved due to constraint violation")
    fun `category is not saved due to constraint violation`(violation: Violation) {
        assertThat(problem)
            .isNotNull()
            .isInstanceOf(CategoriesProblem.ValidationErrors::class)
            .containsAtLeast(violation)
    }

    @Then("unknown category problem occurs")
    fun `unknown category problem occurs`() {
        assertThat(problem)
            .isNotNull()
            .isInstanceOf(CategoriesProblem.Unknown::class)
    }
}
