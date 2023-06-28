package com.smtm.application.service

import com.smtm.application.World
import com.smtm.application.domain.Icon
import com.smtm.application.domain.Violation
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.categories.newCategoryOf
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.*

class CategoriesServiceStepdefs(private val world: World) {

    private val service: CategoriesService
        get() = CategoriesService(world.categoriesRepository)

    private var categoryResult: Category? = null
    private var categoriesResult: Categories? = null
    private var problem: CategoriesProblem? = null

    @When("user saves category with name {string}")
    fun `user saves category with name`(name: String) {
        service.save(newCategoryOf(name, Icon.FOLDER))
    }

    @When("user saves category with empty name")
    fun `user saves category with empty name`() {
        service.save(newCategoryOf("", Icon.FOLDER))
    }

    @When("user saves category")
    fun `user saves category`(category: Category) {
        service.save(category)
    }

    @When("used deletes category of id {long}")
    fun `used deletes category of id N`(id: Long) {
        service.delete(id)
    }

    @Then("category is not saved due to constraint violation")
    fun `category is not saved due to constraint violation`(violation: Violation) {
        assertThat(categoryResult).isNull()
        assertThat(problem).isInstanceOf(CategoriesProblem.Violations::class.java)
        assertThat(problem?.asViolations()?.violations).contains(violation)
    }

    @Then("saved category is")
    fun `saved category is`(category: Category) {
        assertThat(problem).isNull()
        assertThat(this.categoryResult).isEqualTo(category)
    }

    @Then("category is not deleted because it is unknown")
    fun `category is not deleted because it is unknown`() {
        assertThat(problem).isInstanceOf(CategoriesProblem.Unknown::class.java)
    }

    @Then("user categories contains")
    fun `user categories contains`(categories: List<Category>) {
        assertThat(problem).isNull()
        assertThat(categoriesResult?.current).containsExactlyElementsOf(categories)
    }

    private fun CategoriesService.save(category: Category) = save(category, world.ownerId)
        .onRight { this@CategoriesServiceStepdefs.categoryResult = it }
        .onLeft { this@CategoriesServiceStepdefs.problem = it }

    private fun CategoriesService.delete(id: Long) = delete(id, world.ownerId)
        .onRight { this@CategoriesServiceStepdefs.categoriesResult = it }
        .onLeft { this@CategoriesServiceStepdefs.problem = it }
}

private fun CategoriesProblem.asViolations() = this as CategoriesProblem.Violations