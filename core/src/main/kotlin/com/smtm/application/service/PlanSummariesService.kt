package com.smtm.application.service

import arrow.core.Either
import com.smtm.application.api.PlanSummariesApi
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.plans.PlanSummaries
import com.smtm.application.domain.plans.PlanSummariesProblem
import com.smtm.application.spi.PlansRepository

internal class PlanSummariesService(private val plansRepository: PlansRepository) : PlanSummariesApi {

    override fun getAllPlans(ownerId: OwnerId): Either<PlanSummariesProblem, PlanSummaries> {
        return plansRepository.getAllPlanSummaries(ownerId)
    }
}