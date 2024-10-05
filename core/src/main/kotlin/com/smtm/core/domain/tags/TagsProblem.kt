package com.smtm.core.domain.tags

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Violation

sealed interface TagsProblem {

    data class Failure(val throwable: Throwable) : TagsProblem

    data class ValidationErrors(val violations: Collection<Violation>): TagsProblem, Iterable<Violation> {

        override fun iterator(): Iterator<Violation> {
            return violations.iterator()
        }
    }

    data class Unknown(val id: EntityId): TagsProblem

    companion object {

        fun from(throwable: Throwable): TagsProblem =
            Failure(throwable)

        fun from(violations: Collection<Violation>): TagsProblem =
            ValidationErrors(violations)

        fun unknown(id: EntityId): TagsProblem =
            Unknown(id)
    }
}
