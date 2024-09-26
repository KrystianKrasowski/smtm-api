package com.smtm.core.service

import arrow.core.Either
import arrow.core.flatMap
import com.smtm.core.api.PlansApi
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.core.domain.plans.PlansProblem
import com.smtm.core.spi.CategoriesRepository
import com.smtm.core.spi.PlansRepository

internal class PlansService(
    private val categoriesRepository: CategoriesRepository,
    private val plansRepository: PlansRepository
) : PlansApi {

    override fun store(header: PlanHeader, entries: List<Plan.Entry>): Either<PlansProblem, Plan> =
        getCategories()
            .flatMap { Plan.newValidated(header, entries, it.toList()) }
            .flatMap { plansRepository.save(it) }

    private fun getCategories(): Either<PlansProblem, Categories> =
        categoriesRepository
            .getCategories()
            .mapLeft { PlansProblem.categoriesFetchingFailure() }
}
