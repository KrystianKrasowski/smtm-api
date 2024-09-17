package com.smtm.infrastructure.adapters

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.core.api.PlanHeaders
import com.smtm.core.api.PlansQueries
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.core.domain.plans.PlansProblem
import com.smtm.infrastructure.persistence.plans.FullPlanViewRecord
import com.smtm.infrastructure.persistence.plans.PlanRecord
import com.smtm.infrastructure.persistence.toMonetaryAmount
import javax.sql.DataSource
import org.springframework.jdbc.core.JdbcTemplate
import java.time.LocalDate

class PlansAdapter(dataSource: DataSource) : PlansQueries {

    private val jdbc = JdbcTemplate(dataSource)

    override fun getPlanHeadersBy(criteria: PlansQueries.Criteria): Either<Throwable, PlanHeaders> =
        criteria.byDateWithinPeriod
            ?.let { getOwnerPlanHeadersByMatchingDate(criteria.byOwner.value, it) }
            ?: getAllOwnerPlanHeaders(criteria.byOwner.value)

    override fun getPlan(id: EntityId, owner: OwnerId): Either<PlansProblem, Plan> =
        FullPlanViewRecord.runCatching { selectByIdAndOwner(id.toString(), owner.value, jdbc) }
            .map { it.toPlanOrNotFound(id) }
            .getOrElse { PlansProblem.Failure(it).left() }

    private fun getOwnerPlanHeadersByMatchingDate(
        ownerId: Long,
        matchingDate: LocalDate
    ): Either<Throwable, PlanHeaders> =
        PlanRecord.runCatching { selectByOwnerAndMatchingDate(ownerId, matchingDate, jdbc) }
            .map { it.toPlanDefinitions() }
            .map { it.right() }
            .getOrElse { it.left() }

    private fun getAllOwnerPlanHeaders(ownerId: Long): Either<Throwable, PlanHeaders> =
        PlanRecord.runCatching { selectByOwnerId(ownerId, jdbc) }
            .map { it.toPlanDefinitions() }
            .map { it.right() }
            .getOrElse { it.left() }
}

private fun List<PlanRecord>.toPlanDefinitions() = map { it.toPlanDefinition() }

private fun List<FullPlanViewRecord>.toPlanOrNotFound(id: EntityId): Either<PlansProblem.NotFound, Plan> {
    if (isEmpty()) {
        return PlansProblem.NotFound(id).left()
    }

    val first = first()

    return Plan(
        header = PlanHeader(
            id = EntityId.of(first.planId),
            name = first.name,
            period = first.start..first.end
        ),
        entries = map {
            Plan.Entry(
                category = Category(
                    id = EntityId.of(it.categoryId),
                    name = it.categoryName,
                    icon = Icon.valueOfOrNull(it.categoryIcon) ?: Icon.FOLDER
                ),
                value = it.amount.toMonetaryAmount(it.currency)
            )
        }
    ).right()
}
