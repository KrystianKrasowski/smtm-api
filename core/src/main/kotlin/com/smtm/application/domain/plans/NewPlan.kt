package com.smtm.application.domain.plans

import com.smtm.application.domain.NumericId
import com.smtm.application.domain.categories.Category
import javax.money.MonetaryAmount

data class NewPlan(
    val definition: PlanDefinition,
    val entries: List<Entry>
) {

    fun createPlannedCategoriesWith(categories: List<Category>): List<PlannedCategory> =
        entries.mapNotNull { it.toPlannedCategoryOrNull(categories) }

    data class Entry(val categoryId: NumericId, val value: MonetaryAmount) {

        fun toPlannedCategoryOrNull(categories: List<Category>): PlannedCategory? =
            categories
                .firstOrNull { it.id == categoryId }
                ?.let { PlannedCategory(it, value) }
    }
}