package com.smtm.application.domain.categories

import arrow.core.Either
import com.smtm.application.domain.Violation
import com.smtm.application.domain.categories.assertions.assertThat
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.versionOf
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class UserCategoriesStepdefs {

    private lateinit var userCurrentCategories: List<Category>
    private lateinit var categories: Either<CategoriesProblem, Categories>

    @Given("user has categories defined")
    fun `user has categories defined`(categories: List<Category>) {
        userCurrentCategories = categories
    }

    @When("user creates category")
    fun `user creates category`(category: Category) {
        categories = Categories(ownerIdOf(1), versionOf(1), userCurrentCategories)
            .add(category)
    }

    @Then("user categories contains")
    fun `user categories contains`(category: Category) {
        assertThat(categories)
            .isSuccess()
            .contains(category)
    }

    @Then("constraint violations set contains")
    fun `constraint violations set contains`(violation: Violation) {
        assertThat(categories)
            .isViolationsProblem()
            .contains(violation)
    }
}

