package com.smtm.application.domain.plans

import arrow.core.Nel
import com.smtm.application.domain.Violation

sealed interface PlansProblem {

    data class Violations(val violations: Nel<Violation>) : PlansProblem
    data class RepositoryProblem(val throwable: Throwable? = null) : PlansProblem
}