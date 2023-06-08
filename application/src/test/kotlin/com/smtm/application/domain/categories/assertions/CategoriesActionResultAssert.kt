package com.smtm.application.domain.categories.assertions

import com.smtm.application.domain.Violation
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.CategoriesActionResult
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions

class CategoriesActionResultAssert(result: CategoriesActionResult) :
    AbstractAssert<CategoriesActionResultAssert, CategoriesActionResult>(
        result,
        CategoriesActionResultAssert::class.java
    ) {

    fun isSuccess(): CategoriesAssert {
        val categories = actual.getOrNull()

        if (categories == null) {
            failWithMessage("Expected categories action result to be success, but was $actual")
        }

        return CategoriesAssert(categories!!)
    }

    fun isViolationsProblem(): CategoryViolationsProblemAssert {
        val problem = actual.leftOrNull()

        if (problem == null) {
            failWithMessage("Expected categories action result to be failure")
        }

        if (problem !is CategoriesProblem.Violations) {
            failWithMessage("Expected categories action result to be violations of constraints, but was ${problem!!.javaClass}")
        }

        return CategoryViolationsProblemAssert(problem as CategoriesProblem.Violations)
    }
}

class CategoriesAssert(result: Categories) :
    AbstractAssert<CategoriesAssert, Categories>(result, CategoriesAssert::class.java) {

    fun contains(category: Category): CategoriesAssert {
        Assertions.assertThat(actual.categories).contains(category)
        return myself
    }
}

class CategoryViolationsProblemAssert(result: CategoriesProblem.Violations) :
    AbstractAssert<CategoryViolationsProblemAssert, CategoriesProblem.Violations>(
        result,
        CategoryViolationsProblemAssert::class.java
    ) {

    fun contains(violation: Violation): CategoryViolationsProblemAssert {
        if (!actual.violations.contains(violation)) {
            failWithMessage("Expected violations to contain $violation")
        }

        return myself
    }
}

fun assertThat(actual: CategoriesActionResult) = CategoriesActionResultAssert(actual)