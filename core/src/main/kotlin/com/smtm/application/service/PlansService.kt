package com.smtm.application.service

import arrow.core.Either
import arrow.core.flatMap
import com.smtm.application.api.PlansApi
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlanDefinition
import com.smtm.application.domain.plans.PlannedCategory
import com.smtm.application.domain.plans.PlansProblem
import com.smtm.application.spi.PlansRepository

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
