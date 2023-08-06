package com.smtm.application.spi

import arrow.core.Either
import arrow.core.right
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlansProblem

class FakePlansRepository : PlansRepository {

    override fun find(id: NumericId): Either<PlansProblem, Plan> {
        TODO("Not yet implemented")
    }

    override fun save(plan: Plan): Either<PlansProblem, Plan> {
        return plan.right()
    }
}
