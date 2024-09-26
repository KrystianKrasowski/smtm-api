package com.smtm.core.domain.plans

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Violation

sealed interface PlansProblem {

    data class Failure(val throwable: Throwable) : PlansProblem

    data class NotFound(val id: EntityId): PlansProblem

    data class ValidationErrors(val violations: Collection<Violation>): PlansProblem, Iterable<Violation> {

        override fun iterator(): Iterator<Violation> {
            return violations.iterator()
        }
    }

    object CategoriesFetchingFailure: PlansProblem

    companion object {

        fun failure(error: Throwable): PlansProblem =
            Failure(error)

        fun notFound(id: EntityId): PlansProblem =
            NotFound(id)

        fun categoriesFetchingFailure(): PlansProblem =
            CategoriesFetchingFailure

        fun validationErrors(violations: Collection<Violation>): PlansProblem =
            ValidationErrors(violations)
    }
}
