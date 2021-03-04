package com.smtm.application.transactions.categories

import com.smtm.application.transactions.categories.v1.*

private val defaultViolation = constraintViolationOf(
    property = "none",
    message = messageOf(
        pattern = "none"
    )
)

class FakeCategoriesRegister : CategoriesRegister {

    var violation: ConstraintViolation = defaultViolation

    override fun accept(category: Category): AcceptingCategory = violation
        .takeUnless { it == defaultViolation }
        ?.let { listOf(it) }
        ?.let { declinedCategoryOf(it) }
        ?: category
            .copy(id = 1)
            .let { acceptedCategoryOf(it) }


    fun setViolationProperty(property: String) {
        violation = violation.copy(property = property)
    }

    fun setViolationMessage(pattern: String, parameters: Map<String, String>) {
        violation = violation.copy(message = messageOf(pattern, parameters))
    }
}
