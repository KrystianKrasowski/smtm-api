package com.smtm.transactions.spi

import com.smtm.transactions.World
import com.smtm.transactions.api.Category
import io.cucumber.java.en.Given

class CategoriesRepositoryStepdefs(private val world: World) {

    @Given("categories repository contains")
    fun `categories repository contains`(categories: List<Category>) {
        world.categoryRepository.registeredCategories.addAll(categories)
    }
}
