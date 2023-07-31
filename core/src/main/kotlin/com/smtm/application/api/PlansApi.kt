package com.smtm.application.api

import arrow.core.Either
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.plans.NewPlan
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlansProblem

interface PlansApi {

    fun create(plan: NewPlan, ownerId: OwnerId): Either<PlansProblem, Plan>
}