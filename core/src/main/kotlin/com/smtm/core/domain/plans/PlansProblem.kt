package com.smtm.core.domain.plans

import arrow.core.Nel
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.Violation

sealed interface PlansProblem {

    data class Violations(val violations: Nel<Violation>) : PlansProblem
    data class RepositoryProblem(val throwable: Throwable? = null) : PlansProblem
    data class UnknownPlan(val id: NumericId) : PlansProblem
}
