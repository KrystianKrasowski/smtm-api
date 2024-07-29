package com.smtm.core.spi

import arrow.core.Either
import arrow.core.right
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanDefinition
import com.smtm.core.domain.plans.PlansProblem

class FakePlansRepository(private val categoriesRepository: FakeCategoriesRepository) : PlansRepository {

    override fun find(id: NumericId): Either<PlansProblem, Plan> {
        TODO("Not yet implemented")
    }

    override fun findOrPrepare(definition: PlanDefinition, ownerId: OwnerId): Either<PlansProblem, Plan> {
        return Plan.prepare(ownerId, definition, categoriesRepository.list).right()
    }

    override fun save(plan: Plan): Either<PlansProblem, Plan> {
        return plan.right()
    }
}
