package com.smtm.core.api

import arrow.core.Either
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.core.domain.plans.PlansProblem
import com.smtm.core.service.PlansService
import com.smtm.core.spi.CategoriesRepository
import com.smtm.core.spi.PlansRepository

interface PlansApi {

    fun store(header: PlanHeader, entries: List<Plan.Entry>): Either<PlansProblem, Plan>

    companion object {

        fun of(categoriesRepository: CategoriesRepository, plansRepository: PlansRepository): PlansApi =
            PlansService(categoriesRepository, plansRepository)
    }
}
