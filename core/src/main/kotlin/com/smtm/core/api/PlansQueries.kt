package com.smtm.core.api

import arrow.core.Either
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.core.domain.plans.PlansQueriesCriteria
import java.time.LocalDate

typealias PlanHeaders = List<PlanHeader>

interface PlansQueries {

    fun getPlanHeadersBy(criteria: Criteria): Either<Throwable, PlanHeaders>

    interface Criteria {
        val byOwner: OwnerId
        val byDateWithinPeriod: LocalDate?

        companion object {

            fun by(
                owner: OwnerId,
                dateWithinPeriod: LocalDate? = null
            ): Criteria {
                return PlansQueriesCriteria(owner, dateWithinPeriod)
            }
        }
    }
}
