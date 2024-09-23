package com.smtm.core.service

import assertk.assertThat
import assertk.assertions.containsAtLeast
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import com.smtm.core.World
import com.smtm.core.domain.Violation
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.domain.categories.Category
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class CategoriesServiceStepdefs(private val world: World) {

    private val categoriesService get() = CategoriesService(world.categoriesRepository)

    private var categories: Categories? = null
    private var problem: CategoriesProblem? = null

    @When("user creates new category")
    fun `user creates new category`(category: Category) {
        categoriesService.create(category)
            .onRight { categories = it }
            .onLeft { problem = it }
    }

    @When("user updates the category")
    fun `user updates the category`(category: Category) {
        categoriesService.update(category)
            .onRight { categories = it }
            .onLeft { problem = it }
    }

    @When("used deletes category of id {string}")
    fun `used deletes category of id N`(id: String) {

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

    @Then("user categories contains")
    fun `user categories contains`(categoryList: List<Category>) {
        assertThat(categories)
            .isNotNull()
            .containsExactlyInAnyOrder(*(categoryList.toTypedArray()))
    }
}
