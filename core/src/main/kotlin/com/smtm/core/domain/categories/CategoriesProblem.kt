package com.smtm.core.domain.categories

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Violation
import com.smtm.core.domain.tags.TagsProblem

sealed interface CategoriesProblem {

    data class Failure(val throwable: Throwable) : CategoriesProblem

    data class ValidationErrors(val violations: Collection<Violation>) : CategoriesProblem, Iterable<Violation> {

        override fun iterator(): Iterator<Violation> =
            violations.iterator()
    }

    data class Unknown(val id: EntityId) : CategoriesProblem

    companion object {

        internal fun from(tagsProblem: TagsProblem): CategoriesProblem =
            when (tagsProblem) {
                is TagsProblem.Failure -> Failure(tagsProblem.throwable)
                is TagsProblem.ValidationErrors -> ValidationErrors(tagsProblem.violations)
                is TagsProblem.Unknown -> Unknown(tagsProblem.id)
            }
    }
}
