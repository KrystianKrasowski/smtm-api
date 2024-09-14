package com.smtm.infrastructure.adapters

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.core.api.PlanHeaders
import com.smtm.core.api.PlansQueries
import com.smtm.infrastructure.persistence.plans.PlanRecord
import javax.sql.DataSource
import org.springframework.jdbc.core.JdbcTemplate
import java.time.LocalDate

class PlanQueriesAdapter(dataSource: DataSource) : PlansQueries {

    private val jdbc = JdbcTemplate(dataSource)

    override fun getPlanHeadersBy(criteria: PlansQueries.Criteria): Either<Throwable, PlanHeaders> {
        return criteria.byDateWithinPeriod
            ?.let { getOwnerPlansByMatchingDate(criteria.byOwner.value, it) }
            ?: getAllOwnerPlans(criteria.byOwner.value)
    }

    private fun getOwnerPlansByMatchingDate(
        ownerId: Long,
        matchingDate: LocalDate
    ): Either<Throwable, PlanHeaders> =
        PlanRecord.runCatching { selectByOwnerAndMatchingDate(ownerId, matchingDate, jdbc) }
            .map { it.toPlanDefinitions() }
            .map { it.right() }
            .getOrElse { it.left() }

    private fun getAllOwnerPlans(ownerId: Long): Either<Throwable, PlanHeaders> =
        PlanRecord.runCatching { selectByOwnerId(ownerId, jdbc) }
            .map { it.toPlanDefinitions() }
            .map { it.right() }
            .getOrElse { it.left() }
}

private fun List<PlanRecord>.toPlanDefinitions() = map { it.toPlanDefinition() }
