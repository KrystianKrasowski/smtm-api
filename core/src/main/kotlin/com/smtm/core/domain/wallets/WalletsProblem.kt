package com.smtm.core.domain.wallets

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Violation

sealed interface WalletsProblem {

    data class Failure(val throwable: Throwable) : WalletsProblem

    data class ValidationErrors(val violations: Collection<Violation>): WalletsProblem, Iterable<Violation> {

        override fun iterator(): Iterator<Violation> {
            return violations.iterator()
        }
    }

    data class Unknown(val id: EntityId): WalletsProblem

    companion object {

        fun failure(throwable: Throwable): WalletsProblem =
            Failure(throwable)

        fun validationError(violations: Collection<Violation>): WalletsProblem =
            ValidationErrors(violations)

        fun unknown(id: EntityId): WalletsProblem =
            Unknown(id)
    }
}
