package com.smtm.core.spi

import com.smtm.core.World
import com.smtm.core.domain.categories.Category
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then

class CategoriesRepositoryStepdefs(private val world: World) {

    @Given("user categories version is {long}")
    fun `user categories version is N`(version: Long) {
    }

    @Given("user categories are")
    fun `user categories are`(categories: List<Category>) {
    }

    @Given("next category id is {long}")
    fun `next category id is N`(id: Long) {
    }

    @Then("user categories version is updated to {long}")
    fun `user categories version is updated to N`(version: Long) {
    }
}
