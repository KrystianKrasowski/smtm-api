package com.smtm.infrastructure.adapters

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.core.api.PlanHeaders
import com.smtm.core.api.PlansQueries
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlansProblem
import com.smtm.core.spi.PlansRepository
import com.smtm.infrastructure.persistence.categories.CategorySetsJpaRepository
import com.smtm.infrastructure.persistence.plans.PlanEntity
import com.smtm.infrastructure.persistence.plans.PlansJpaRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

open class PlansRepositoryJpaAdapter(
    entityManager: EntityManager,
    private val ownerIdProvider: () -> OwnerId,
) : PlansQueries, PlansRepository {

    private val jpaRepositoryFactory = JpaRepositoryFactory(entityManager)
    private val plansJpaRepository = jpaRepositoryFactory.getRepository(PlansJpaRepository::class.java)
    private val categorySetsJpaRepository = jpaRepositoryFactory.getRepository(CategorySetsJpaRepository::class.java)
    private val logger = LoggerFactory.getLogger(PlansRepositoryJpaAdapter::class.java)

    override fun getPlanHeadersBy(criteria: PlansQueries.Criteria): Either<Throwable, PlanHeaders> =
        criteria.byDateWithinPeriod
            ?.let { getByOwnerAndMatchingDate(ownerIdProvider(), it) }
            ?: getByOwner(ownerIdProvider())

    override fun getPlan(id: EntityId): Either<PlansProblem, Plan> =
        plansJpaRepository
            .runCatching { getReferenceById(id.asString()) }
            .mapCatching { createPlan(it) }
            .map { it.right() }
            .onFailure { logger.error("Error while fetching the plan of ID: $id", it) }
            .getOrElse { it.toPlansProblem(id).left() }

    @Transactional
    override fun save(plan: Plan): Either<PlansProblem, Plan> =
        plansJpaRepository
            .runCatching { save(PlanEntity.from(plan, ownerIdProvider())) }
            .map { it.toPlan(plan.categories) }
            .map { it.right() }
            .onFailure { logger.error("Error while saving the plan of ID: $it", it) }
            .getOrElse { it.toPlansProblem().left() }

    @Transactional
    override fun deleteById(id: EntityId): Either<PlansProblem, EntityId> =
        plansJpaRepository
            .runCatching { deleteByEntityId(id.asString()) }
            .map { affectedRows ->
                id.takeUnless { affectedRows == 0 }
                    ?.right()
                    ?: PlansProblem.notFound(id).left()
            }
            .onFailure { logger.error("Error while deleting the plan of ID: $id", it) }
            .getOrElse { it.toPlansProblem().left() }

    private fun getByOwnerAndMatchingDate(owner: OwnerId, date: LocalDate): Either<Throwable, PlanHeaders> =
        plansJpaRepository
            .runCatching { findByOwnerIdAndMatchingDate(date, owner.asString()) }
            .map { it.toPlanHeaders() }
            .map { it.right() }
            .onFailure { logger.error("Error while fetching plan headers.", it) }
            .getOrElse { it.left() }

    private fun getByOwner(owner: OwnerId): Either<Throwable, PlanHeaders> =
        plansJpaRepository
            .runCatching { findAllByOwnerIdOrderByEndDesc(owner.asString()) }
            .map { it.toPlanHeaders() }
            .map { it.right() }
            .onFailure { logger.error("Error while fetching plan headers.", it) }
            .getOrElse { it.left() }

    private fun createPlan(entity: PlanEntity): Plan =
        runCatching { findCategoriesByOwnerId(ownerIdProvider()) }
            .map { entity.toPlan(it) }
            .getOrThrow()

    private fun findCategoriesByOwnerId(owner: OwnerId): List<Category> =
        categorySetsJpaRepository
            .findByOwnerId(owner.asString())
            ?.categories
            ?.map { it.toDomain() }
            ?: emptyList()
}

private fun List<PlanEntity>.toPlanHeaders(): PlanHeaders =
    map { it.toPlanHeader() }

private fun Throwable.toPlansProblem(id: EntityId): PlansProblem =
    when (this) {
        is EntityNotFoundException -> PlansProblem.notFound(id)
        else -> toPlansProblem()
    }

private fun Throwable.toPlansProblem(): PlansProblem =
    PlansProblem.failure(this)
