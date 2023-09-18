package com.smtm.infrastructure.persistence

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.application.api.PlansQueries
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlanDefinition
import com.smtm.application.domain.plans.PlansProblem
import com.smtm.application.spi.PlansRepository
import com.smtm.infrastructure.persistence.categories.CategoryRecord
import com.smtm.infrastructure.persistence.categories.Conversions.toCategoryList
import com.smtm.infrastructure.persistence.plans.PlanEntryRecord
import com.smtm.infrastructure.persistence.plans.PlanRecord
import com.smtm.infrastructure.persistence.plans.PlannedCategoriesView
import javax.sql.DataSource
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.time.Clock
import java.time.LocalDateTime

class PlansRepositoryJdbcAdapter(
    dataSource: DataSource,
    private val clock: Clock
) : PlansQueries, PlansRepository {

    private val jdbc = JdbcTemplate(dataSource)
    private val transactions = TransactionTemplate(DataSourceTransactionManager(dataSource))

    override fun getCurrentPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>> =
        jdbc.runCatching { PlanRecord.getByOwnerIdAndPeriodAround(ownerId.value, LocalDateTime.now(clock), this) }
            .toQueryResult()

    override fun getUpcomingPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>> =
        jdbc.runCatching { PlanRecord.getByOwnerIdAndPeriodAfter(ownerId.value, LocalDateTime.now(clock), this) }
            .toQueryResult()

    override fun getArchivedPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>> =
        jdbc.runCatching { PlanRecord.getByOwnerIdAndPeriodBefore(ownerId.value, LocalDateTime.now(clock), this) }
            .toQueryResult()

    override fun find(id: NumericId): Either<PlansProblem, Plan> =
        id.takeIf { it.isSettled() }
            ?.let { PlannedCategoriesView.selectByPlanId(it.value, jdbc) }
            ?.takeUnless { it.empty }
            ?.let { it.toPlan(CategoryRecord.selectByOwnerId(it.ownerId, jdbc)) }
            ?.right()
            ?: PlansProblem.UnknownPlan(id).left()

    override fun findOrPrepare(definition: PlanDefinition, ownerId: OwnerId): Either<PlansProblem, Plan> =
        definition
            .takeIf { it.settled }
            ?.let { PlannedCategoriesView.selectByPlanId(it.id.value, jdbc) }
            ?.takeUnless { it.empty }
            ?.let { it.toPlan(CategoryRecord.selectByOwnerId(it.ownerId, jdbc)) }
            ?.right()
            ?: getAvailableCategories(ownerId)
                .map { Plan.prepare(ownerId, definition, it) }

    override fun save(plan: Plan): Either<PlansProblem, Plan> =
        transactions.execute { trn ->
            plan.runCatching { upsertPlanDefinition(plan) }
                .mapCatching { removeExistingEntries(it) }
                .mapCatching { addNewEntries(it) }
                .map { getPlan(it.id) }
                .onFailure { logger.error(it.message, it) }
                .onFailure { trn.setRollbackOnly() }
                .getOrElse { PlansProblem.RepositoryProblem(it).left() }
        }!!

    private fun Result<List<PlanRecord>>.toQueryResult() =
        this.map { it.toPlanDefinitionList() }
            .map { it.right() }
            .onFailure { logger.error("Cannot fetch plan definitions", it) }
            .getOrElse { it.left() }

    private fun List<PlanRecord>.toPlanDefinitionList() =
        this.map { it.toPlanDefinition() }

    private fun upsertPlanDefinition(plan: Plan): Plan =
        PlanRecord.from(plan, jdbc)
            .copy(version = plan.version.increment().value)
            .upsert()
            .toPlanDefinition()
            .let { plan.copy(definition = it) }

    private fun removeExistingEntries(plan: Plan): Plan =
        plan.takeIf { it.settled && it.notEmpty }
            ?.also { PlanEntryRecord.deleteByPlanId(plan.id.value, jdbc) }
            ?: plan

    private fun addNewEntries(plan: Plan): Plan =
        plan.entries
            .map { PlanEntryRecord.from(it, plan, jdbc) }
            .map { it.insert() }
            .let { plan }

    private fun getPlan(id: NumericId): Either<PlansProblem, Plan> =
        find(id)
            .mapLeft { IllegalStateException("Cannot find plan by id ${id.value}") }
            .mapLeft { PlansProblem.RepositoryProblem(it) }

    private fun getAvailableCategories(ownerId: OwnerId): Either<PlansProblem, List<Category>> =
        jdbc.runCatching { CategoryRecord.selectByOwnerId(ownerId.value, this) }
            .map { it.toCategoryList() }
            .map { it.right() }
            .getOrElse { PlansProblem.RepositoryProblem().left() }
}

private val logger = LoggerFactory.getLogger(PlansRepositoryJdbcAdapter::class.java)
