package com.smtm.application.spring.infrastructure.persistence.plans

import com.smtm.application.domain.plans.PlannedCategory
import java.math.BigDecimal

data class PlanEntryEntity(
    val id: Long?,
    val planId: Long,
    val categoryId: Long,
    val amount: Int,
    val currency: String
) {

    companion object {

        fun of(planId: Long, plannedCategory: PlannedCategory) = PlanEntryEntity(
            id = null,
            planId = planId,
            categoryId = plannedCategory.category.id.value,
            amount = plannedCategory.value.multiply(BigDecimal.TEN.pow(plannedCategory.value.currency.defaultFractionDigits)).number.intValueExact(),
            currency = plannedCategory.value.currency.currencyCode
        )
    }
}
