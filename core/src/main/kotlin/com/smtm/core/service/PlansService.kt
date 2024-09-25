package com.smtm.core.service

import arrow.core.Either
import arrow.core.flatMap
import com.smtm.core.api.PlansApi
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.core.domain.plans.PlansProblem
import com.smtm.core.spi.PlansRepository

internal class PlansService(
    private val plansRepository: PlansRepository
) : PlansApi {

    override fun store(header: PlanHeader, entries: List<Plan.Entry>): Either<PlansProblem, Plan> =
        Plan.validated(header, entries)
            .flatMap { plansRepository.save(it) }
}
