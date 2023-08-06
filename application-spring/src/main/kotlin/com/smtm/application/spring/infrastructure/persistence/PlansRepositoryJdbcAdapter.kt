package com.smtm.application.spring.infrastructure.persistence

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.application.api.PlansQueries
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlanDefinition
import com.smtm.application.domain.plans.PlannedCategory
import com.smtm.application.domain.plans.PlansProblem
import com.smtm.application.spi.PlansRepository
import com.smtm.application.spring.infrastructure.persistence.categories.CategoriesResultSet
import com.smtm.application.spring.infrastructure.persistence.plans.PlanEntity
import com.smtm.application.spring.infrastructure.persistence.plans.PlanEntriesJoinedResultSet
import com.smtm.application.spring.infrastructure.persistence.plans.PlanEntryEntity
import com.smtm.application.spring.infrastructure.persistence.plans.PlansJdbcRepository
import com.smtm.application.spring.infrastructure.persistence.plans.PlansResultSet
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.transaction.support.TransactionOperations
import java.time.Clock
import java.time.LocalDateTime

class PlansRepositoryJdbcAdapter(
    private val clock: Clock,
    private val jdbc: JdbcOperations,
    private val transactions: TransactionOperations
) : PlansQueries, PlansRepository {

    private val plansRepository = PlansJdbcRepository(clock, jdbc)

    override fun getCurrentPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>> =
        jdbc.runCatching { PlansResultSet.getByOwnerIdAndPeriodAround(ownerId.value, LocalDateTime.now(clock), this) }
            .toQueryResult()

    override fun getUpcomingPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>> =
        jdbc.runCatching { PlansResultSet.getByOwnerIdAndPeriodAfter(ownerId.value, LocalDateTime.now(clock), this) }
            .toQueryResult()

    override fun getArchivedPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>> =
        jdbc.runCatching { PlansResultSet.getByOwnerIdAndPeriodBefore(ownerId.value, LocalDateTime.now(clock), this) }
            .toQueryResult()

    override fun find(id: NumericId): Either<PlansProblem, Plan> =
        PlanEntriesJoinedResultSet.selectByPlanId(id.value, jdbc)
            .takeUnless { it.empty }
            ?.let { it.toPlan(CategoriesResultSet.selectByOwnerId(it.ownerId, jdbc)) }
            ?.right()
            ?: PlansProblem.UnknownPlan(id).left()

    override fun save(plan: Plan): Either<PlansProblem, Plan> =
        plan.takeIf { it.isModificationNeeded() }
            ?.applyChanges()
            ?: plan.right()

    private fun Result<PlansResultSet>.toQueryResult() =
        this.map { it.toPlanDefinitionList() }
            .map { it.right() }
            .onFailure { logger.error("Cannot fetch plan definitions", it) }
            .getOrElse { it.left() }

    private fun Plan.applyChanges(): Either<PlansProblem, Plan> =
        transactions.execute { trn ->
            runCatching { upsertPlanDefinition(this) }
                .mapCatching { addNewEntries(it) }
                .map { getPlan(it.id) }
                .onFailure { trn.setRollbackOnly() }
                .onFailure { logger.error(it.message, it) }
                .getOrElse { PlansProblem.RepositoryProblem(it).left() }
        }!!

    private fun upsertPlanDefinition(plan: Plan): Plan = PlanEntity.of(plan)
        .runCatching { plansRepository.upsert(this) }
        .map { plan.definition.copy(id = NumericId.of(it.id)) }
        .map { plan.copy(definition = it) }
        .getOrThrow()

    private fun addNewEntries(plan: Plan): Plan =
        plan.onEachNewEntry { plansRepository.insert(PlanEntryEntity.of(plan.id.value, it)) }

    private fun getPlan(id: NumericId): Either<PlansProblem, Plan> =
        find(id)
            .mapLeft { IllegalStateException("Cannot find plan by id ${id.value}") }
            .mapLeft { PlansProblem.RepositoryProblem(it) }
}

private val logger = LoggerFactory.getLogger(PlansRepositoryJdbcAdapter::class.java)

private fun Plan.onEachNewEntry(block: (PlannedCategory) -> Unit) = apply { newEntries.onEach(block) }

private fun Plan.isModificationNeeded(): Boolean = newEntries.isNotEmpty()
