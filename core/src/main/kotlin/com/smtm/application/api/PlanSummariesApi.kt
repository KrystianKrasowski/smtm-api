package com.smtm.application.api

import arrow.core.Either
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.plans.PlanSummaries
import com.smtm.application.domain.plans.PlanSummariesProblem
import com.smtm.application.service.PlanSummariesService
import com.smtm.application.spi.PlansRepository

interface PlanSummariesApi {

    fun getAllPlans(ownerId: OwnerId): Either<PlanSummariesProblem, PlanSummaries>

    companion object {

        fun create(plans: PlansRepository): PlanSummariesApi = PlanSummariesService(plans)
    }
}