package com.smtm.application.spi

import arrow.core.Either
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.plans.PlanSummaries
import com.smtm.application.domain.plans.PlanSummariesProblem

interface PlansRepository {

    fun getAllPlanSummaries(ownerId: OwnerId): Either<PlanSummariesProblem, PlanSummaries>
}