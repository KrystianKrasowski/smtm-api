package com.smtm.transactions.categories

import com.smtm.common.ConstraintViolation
import com.smtm.transactions.World
import com.smtm.transactions.api.AcceptingCategory
import com.smtm.transactions.api.Category
import com.smtm.transactions.assertThat
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat

class CategoriesRegisterImplStepdefs(private val world: World) {

    private val register = categoriesRegisterOf(world.categoryRepository)
    private lateinit var acceptingCategory: AcceptingCategory

    @When("category is registering as")
    fun `category is registering as`(category: Category) {
        acceptingCategory = register.accept(category)
    }

    @Then("category {string} is registered")
    fun `category xxx is registered`(name: String) {
        assertThat(world.categoryRepository.hasRegisteredName(name)).isTrue()
    }

    @Then("category is registered as")
    fun `category is registered as`(category: Category) {
        assertThat(world.categoryRepository.registeredCategories).contains(category)
    }

    @Then("category is not registered")
    fun `category is not registered`() {
        assertThat(acceptingCategory).isDeclined()
    }

    @Then("constraint violations contain")
    fun `constraint violations contains`(violation: ConstraintViolation) {
        assertThat(acceptingCategory).containsViolation(violation)
    }
}
