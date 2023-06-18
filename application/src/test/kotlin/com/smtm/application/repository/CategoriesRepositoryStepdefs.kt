package com.smtm.application.repository

import com.smtm.application.World
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.versionOf
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.*

class CategoriesRepositoryStepdefs(private val world: World) {

    private val modifiedCategories get() = world.categoriesRepository.categories

    @Given("user categories version is {long}")
    fun `user categories version is N`(version: Long) {
        world.categoriesRepository.version = versionOf(version)
    }

    @Given("user has categories defined")
    fun `user has categories defined`(categories: List<Category>) {
        world.categoriesRepository.categoryList = categories
    }

    @Then("user categories contain")
    fun `user categories contains`(category: Category) {
        assertThat(modifiedCategories?.list).contains(category)
    }

    @Then("user categories version is incremented to {long}")
    fun `user categories version is incremented to N`(version: Long) {
        assertThat(modifiedCategories?.version).isEqualTo(versionOf(version))
    }
}