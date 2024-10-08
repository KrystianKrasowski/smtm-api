package com.smtm.core.spi

import arrow.core.Either
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlansProblem

interface PlansRepository {

    fun save(plan: Plan): Either<PlansProblem, Plan>

    fun getPlan(id: EntityId): Either<PlansProblem, Plan>

    fun deleteById(id: EntityId): Either<PlansProblem, EntityId>
}
