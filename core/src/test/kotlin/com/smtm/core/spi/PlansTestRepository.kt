package com.smtm.core.spi

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlansProblem

class PlansTestRepository : PlansRepository {

    var plans: MutableList<Plan> = mutableListOf()

    override fun save(plan: Plan): Either<PlansProblem, Plan> =
        plan
            .also { plans.add(it) }
            .right()

    override fun getPlan(id: EntityId): Either<PlansProblem, Plan> =
        plans
            .firstOrNull { it.id == id }
            ?.right()
            ?: PlansProblem.notFound(id).left()

    fun hasPlan(id: String): Boolean =
        plans
            .map { it.id.asString() }
            .contains(id)

}
