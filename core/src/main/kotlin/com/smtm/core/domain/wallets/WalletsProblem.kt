package com.smtm.core.domain.wallets

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Violation
import com.smtm.core.domain.tags.TagsProblem

sealed interface WalletsProblem {

    data class Failure(val throwable: Throwable) : WalletsProblem

    data class ValidationErrors(val violations: Collection<Violation>): WalletsProblem, Iterable<Violation> {

        override fun iterator(): Iterator<Violation> {
            return violations.iterator()
        }
    }

    data class Unknown(val id: EntityId): WalletsProblem

    companion object {

        internal fun from(tagsProblem: TagsProblem): WalletsProblem =
            when (tagsProblem) {
                is TagsProblem.Failure -> Failure(tagsProblem.throwable)
                is TagsProblem.ValidationErrors -> ValidationErrors(tagsProblem.violations)
                is TagsProblem.Unknown -> Unknown(tagsProblem.id)
            }
    }
}
