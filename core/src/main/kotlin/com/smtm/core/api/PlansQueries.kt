package com.smtm.core.api

import arrow.core.Either
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.core.domain.plans.PlansProblem
import com.smtm.core.domain.plans.PlansQueriesCriteria
import java.time.LocalDate

typealias PlanHeaders = List<PlanHeader>

interface PlansQueries {

    fun getPlanHeadersBy(criteria: Criteria): Either<Throwable, PlanHeaders>

    fun getPlan(id: EntityId): Either<PlansProblem, Plan>

    interface Criteria {
        val byDateWithinPeriod: LocalDate?

        companion object {

            fun by(
                dateWithinPeriod: LocalDate? = null
            ): Criteria {
                return PlansQueriesCriteria(dateWithinPeriod)
            }
        }
    }
}
