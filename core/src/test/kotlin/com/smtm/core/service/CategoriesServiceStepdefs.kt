package com.smtm.core.service

import com.smtm.core.World
import com.smtm.core.domain.Icon
import com.smtm.core.domain.Violation
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.domain.categories.Category
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat

class CategoriesServiceStepdefs(private val world: World) {

    private val service: CategoriesService
        get() = CategoriesService(world.categoriesRepository)

    private var categoriesResult: Categories? = null
    private var problem: CategoriesProblem? = null

    @When("user saves category with name {string}")
    fun `user saves category with name`(name: String) {
        service.save(Category.newOf(name = name, icon = Icon.FOLDER))
    }

    @When("user saves category with empty name")
    fun `user saves category with empty name`() {
        service.save(Category.newOf(name = "", icon = Icon.FOLDER))
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
        assertThat(categoriesResult).isNull()
        assertThat(problem).isInstanceOf(CategoriesProblem.Violations::class.java)
        assertThat(problem?.asViolations()?.violations).contains(violation)
    }

    @Then("saved category is")
    fun `saved category is`(category: Category) {
        assertThat(problem).isNull()
        assertThat(categoriesResult?.current).contains(category)
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
        .onRight { this@CategoriesServiceStepdefs.categoriesResult = it }
        .onLeft { this@CategoriesServiceStepdefs.problem = it }

    private fun CategoriesService.delete(id: Long) = delete(id, world.ownerId)
        .onRight { this@CategoriesServiceStepdefs.categoriesResult = it }
        .onLeft { this@CategoriesServiceStepdefs.problem = it }
}

private fun CategoriesProblem.asViolations() = this as CategoriesProblem.Violations
