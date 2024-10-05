package com.smtm.core.service

import arrow.core.Either
import arrow.core.flatMap
import com.smtm.core.api.PlansApi
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.core.domain.plans.PlansProblem
import com.smtm.core.domain.tags.Tags
import com.smtm.core.spi.CategoriesRepository
import com.smtm.core.spi.PlansRepository

internal class PlansService(
    private val categoriesRepository: CategoriesRepository,
    private val plansRepository: PlansRepository
) : PlansApi {

    override fun create(header: PlanHeader, entries: List<Plan.Entry>): Either<PlansProblem, Plan> =
        getCategories()
            .flatMap { Plan.validated(header, entries, it.toList()) }
            .flatMap { plansRepository.save(it) }

    override fun update(header: PlanHeader, entries: List<Plan.Entry>): Either<PlansProblem, Plan> =
        plansRepository
            .getPlan(header.id)
            .map { it.redefine(header) }
            .map { it.redefine(entries) }
            .flatMap { it.validate() }
            .flatMap { plansRepository.save(it) }

    override fun delete(id: EntityId): Either<PlansProblem, EntityId> =
        plansRepository.deleteById(id)

    private fun getCategories(): Either<PlansProblem, Tags<Category>> =
        categoriesRepository
            .getCategories()
            .mapLeft { PlansProblem.categoriesFetchingFailure() }
}
