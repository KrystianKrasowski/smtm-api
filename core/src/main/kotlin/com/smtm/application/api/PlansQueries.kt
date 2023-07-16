package com.smtm.application.api

import arrow.core.Either
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.plans.PlanDefinition

interface PlansQueries {

    fun getCurrentPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>>

    fun getUpcomingPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>>

    fun getArchivedPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>>
}