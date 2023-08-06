package com.smtm.application.service

import arrow.core.Either
import arrow.core.flatMap
import com.smtm.application.api.PlansApi
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.plans.NewPlan
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlansProblem
import com.smtm.application.spi.CategoriesRepository
import com.smtm.application.spi.PlansRepository

internal class PlansService(
    private val categoriesRepository: CategoriesRepository,
    private val plansRepository: PlansRepository
) : PlansApi {

    override fun find(id: NumericId): Either<PlansProblem, Plan> =
        plansRepository.find(id)

    override fun create(plan: NewPlan, ownerId: OwnerId): Either<PlansProblem, Plan> {
        return categoriesRepository
            .getCategories(ownerId)
            .mapLeft { PlansProblem.RepositoryProblem() }
            .map { it.current }
            .map { Plan.prepared(it, ownerId) }
            .flatMap { it.define(plan) }
            .flatMap { plansRepository.save(it) }
    }
}
