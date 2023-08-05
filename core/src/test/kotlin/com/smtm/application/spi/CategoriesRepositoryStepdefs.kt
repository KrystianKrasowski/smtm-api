package com.smtm.application.spi

import com.smtm.application.World
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.versionOf
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.*

class CategoriesRepositoryStepdefs(private val world: World) {

    @Given("user categories version is {long}")
    fun `user categories version is N`(version: Long) {
        world.categoriesRepository.version = versionOf(version)
    }

    @Given("user categories are")
    fun `user categories are`(categories: List<Category>) {
        world.categoriesRepository.list = categories
    }

    @Given("next category id is {long}")
    fun `next category id is N`(id: Long) {
        world.categoriesRepository.nextCategoryId = NumericId.of(id)
    }

    @Then("user categories version is updated to {long}")
    fun `user categories version is updated to N`(version: Long) {
        assertThat(world.categoriesRepository.savedCategories?.version).isEqualTo(versionOf(version))
    }
}