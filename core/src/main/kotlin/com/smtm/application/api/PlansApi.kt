package com.smtm.application.api

import arrow.core.Either
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.plans.NewPlan
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlansProblem
import com.smtm.application.service.PlansService
import com.smtm.application.spi.CategoriesRepository
import com.smtm.application.spi.PlansRepository

interface PlansApi {

    fun find(id: NumericId): Either<PlansProblem, Plan>

    fun create(plan: NewPlan, ownerId: OwnerId): Either<PlansProblem, Plan>

    companion object {

        fun create(categoriesRepository: CategoriesRepository, plansRepository: PlansRepository): PlansApi =
            PlansService(categoriesRepository, plansRepository)
    }
}
