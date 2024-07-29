package com.smtm.core.service

import arrow.core.Either
import arrow.core.flatMap
import com.smtm.core.api.PlansApi
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanDefinition
import com.smtm.core.domain.plans.PlannedCategory
import com.smtm.core.domain.plans.PlansProblem
import com.smtm.core.spi.PlansRepository

internal class PlansService(private val plansRepository: PlansRepository) : PlansApi {

    override fun find(id: NumericId): Either<PlansProblem, Plan> =
        plansRepository.find(id)

    override fun save(
        definition: PlanDefinition,
        categories: List<PlannedCategory>,
        ownerId: OwnerId
    ): Either<PlansProblem, Plan> =
        plansRepository
            .findOrPrepare(definition, ownerId)
            .flatMap { it.define(definition) }
            .flatMap { it.replace(categories) }
            .flatMap { plansRepository.save(it) }
}
