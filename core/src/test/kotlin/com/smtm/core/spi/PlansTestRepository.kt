package com.smtm.core.spi

import arrow.core.Either
import arrow.core.right
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlansProblem

class PlansTestRepository : PlansRepository {

    private val savedPlans: MutableList<Plan> = mutableListOf()

    override fun save(plan: Plan): Either<PlansProblem, Plan> =
        plan
            .also { savedPlans.add(it) }
            .right()

    fun hasPlan(id: String): Boolean =
        savedPlans
            .map { it.id.asString() }
            .contains(id)
}
