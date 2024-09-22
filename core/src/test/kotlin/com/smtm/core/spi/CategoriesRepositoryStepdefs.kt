package com.smtm.core.spi

import com.smtm.core.World
import com.smtm.core.domain.categories.Category
import io.cucumber.java.en.Given

class CategoriesRepositoryStepdefs(private val world: World) {

    @Given("user categories version is {int}")
    fun `user categories version is N`(version: Int) {
        world.categoriesRepository.setVersion(version)
    }

    @Given("user categories are")
    fun `user categories are`(categories: List<Category>) {
        world.categoriesRepository.setCategories(categories)
    }
}
