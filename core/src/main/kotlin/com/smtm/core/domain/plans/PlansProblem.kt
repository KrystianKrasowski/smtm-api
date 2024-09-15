package com.smtm.core.domain.plans

import com.smtm.core.domain.NumericId

sealed interface PlansProblem {

    data class Failure(val throwable: Throwable) : PlansProblem
    data class NotFound(val id: NumericId): PlansProblem
}
