package com.smtm.core.service

import com.smtm.core.World
import com.smtm.core.domain.Violation
import com.smtm.core.domain.categories.Category
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class CategoriesServiceStepdefs(private val world: World) {


    @When("user saves category with name {string}")
    fun `user saves category with name`(name: String) {

    }

    @When("user saves category with empty name")
    fun `user saves category with empty name`() {

    }

    @When("user saves category")
    fun `user saves category`(category: Category) {

    }

    @When("used deletes category of id {string}")
    fun `used deletes category of id N`(id: String) {

    }

    @Then("category is not saved due to constraint violation")
    fun `category is not saved due to constraint violation`(violation: Violation) {

    }

    @Then("saved category is")
    fun `saved category is`(category: Category) {

    }

    @Then("category is not deleted because it is unknown")
    fun `category is not deleted because it is unknown`() {
    }

    @Then("user categories contains")
    fun `user categories contains`(categories: List<Category>) {
    }

}
