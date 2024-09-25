package com.smtm.core.api

import arrow.core.Either
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.core.domain.plans.PlansProblem

interface PlansApi {

    fun store(header: PlanHeader, entries: List<Plan.Entry>): Either<PlansProblem, Plan>
}
