package com.smtm.core.domain.categories

import com.smtm.core.domain.Violation

sealed interface CategoriesProblem {

    data class Failure(val throwable: Throwable) : CategoriesProblem

    data class ValidationErrors(val violations: Collection<Violation>): CategoriesProblem, Iterable<Violation> {

        override fun iterator(): Iterator<Violation> {
            return violations.iterator()
        }
    }

    data class Unknown(val category: Category): CategoriesProblem

    companion object {

        fun failure(throwable: Throwable): CategoriesProblem =
            Failure(throwable)

        fun validationError(violations: Collection<Violation>): CategoriesProblem =
            ValidationErrors(violations)

        fun unknown(category: Category): CategoriesProblem =
            Unknown(category)
    }
}
