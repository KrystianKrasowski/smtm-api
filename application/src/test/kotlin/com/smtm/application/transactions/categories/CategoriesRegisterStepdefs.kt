package com.smtm.application.transactions.categories

import io.cucumber.java.en.Given

class CategoriesRegisterStepdefs(private val fakeCategoriesRegister: FakeCategoriesRegister) {

    @Given("category violates {string} property constraint")
    fun `category constraint violation concerns the xxx property`(property: String) {
        fakeCategoriesRegister.setViolationProperty(property)
    }

    @Given("violation message pattern is {string} with parameters")
    fun `violation message pattern is xxx with parameters`(messagePattern: String, parameters: Map<String, String>) {
        fakeCategoriesRegister.setViolationMessage(messagePattern, parameters)
    }
}
