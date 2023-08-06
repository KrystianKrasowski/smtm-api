package com.smtm.application.spi

import arrow.core.Either
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlansProblem

interface PlansRepository {

    fun find(id: NumericId): Either<PlansProblem, Plan>

    fun save(plan: Plan): Either<PlansProblem, Plan>
}
