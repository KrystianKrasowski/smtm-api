package com.smtm.core.api

import arrow.core.Either
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.plans.PlanDefinition

interface PlansQueries {

    fun getCurrentPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>>

    fun getUpcomingPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>>

    fun getArchivedPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>>
}
