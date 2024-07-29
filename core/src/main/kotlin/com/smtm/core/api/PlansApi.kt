package com.smtm.core.api

import arrow.core.Either
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanDefinition
import com.smtm.core.domain.plans.PlannedCategory
import com.smtm.core.domain.plans.PlansProblem
import com.smtm.core.service.PlansService
import com.smtm.core.spi.PlansRepository

interface PlansApi {

    fun find(id: NumericId): Either<PlansProblem, Plan>

    fun save(
        definition: PlanDefinition,
        categories: List<PlannedCategory>,
        ownerId: OwnerId
    ): Either<PlansProblem, Plan>

    companion object {

        fun create(plansRepository: PlansRepository): PlansApi =
            PlansService(plansRepository)
    }
}
