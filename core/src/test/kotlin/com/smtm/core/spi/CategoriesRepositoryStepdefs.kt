package com.smtm.core.spi

import com.smtm.core.World
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.versionOf
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat

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
