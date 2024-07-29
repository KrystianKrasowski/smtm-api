package com.smtm.core.spi

import arrow.core.Either
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanDefinition
import com.smtm.core.domain.plans.PlansProblem

interface PlansRepository {

    fun find(id: NumericId): Either<PlansProblem, Plan>

    fun findOrPrepare(definition: PlanDefinition, ownerId: OwnerId): Either<PlansProblem, Plan>

    fun save(plan: Plan): Either<PlansProblem, Plan>
}
