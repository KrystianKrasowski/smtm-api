package com.smtm.core.service

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.smtm.core.api.PlansApi
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.core.domain.plans.PlansProblem
import com.smtm.core.spi.PlansRepository

internal class PlansService(
    private val plansRepository: PlansRepository
) : PlansApi {

    override fun create(header: PlanHeader, entries: List<Plan.Entry>): Either<PlansProblem, Plan> {
        return Plan.of(header, entries)
            .right()
            .flatMap { plansRepository.save(it) }
    }
}
