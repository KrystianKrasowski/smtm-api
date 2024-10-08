package com.smtm.core.api

import arrow.core.Either
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.core.domain.plans.PlansProblem
import com.smtm.core.service.PlansService
import com.smtm.core.spi.CategoriesRepository
import com.smtm.core.spi.PlansRepository

interface PlansApi {

    fun create(header: PlanHeader, entries: List<Plan.Entry>): Either<PlansProblem, Plan>

    fun update(header: PlanHeader, entries: List<Plan.Entry>): Either<PlansProblem, Plan>

    fun delete(id: EntityId): Either<PlansProblem, EntityId>

    companion object {

        fun of(categoriesRepository: CategoriesRepository, plansRepository: PlansRepository): PlansApi =
            PlansService(categoriesRepository, plansRepository)
    }
}
