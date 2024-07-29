package com.smtm.core.domain.plans

import com.smtm.core.domain.categories.Category
import javax.money.MonetaryAmount

data class PlannedCategory(
    val category: Category,
    val value: MonetaryAmount
)
