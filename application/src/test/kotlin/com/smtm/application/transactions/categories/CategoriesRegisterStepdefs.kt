package com.smtm.application.transactions.categories

import com.smtm.application.transactions.categories.v1.declinedCategoryOf
import com.smtm.security.registration.ConstraintViolation
import io.cucumber.java.en.Given

class CategoriesRegisterStepdefs(private val fakeCategoriesRegister: FakeCategoriesRegister) {

    @Given("category constraint violations are")
    fun `category constraint violations are`(violations: List<ConstraintViolation>) {
        fakeCategoriesRegister.declinedCategory = declinedCategoryOf(violations)
    }
}
