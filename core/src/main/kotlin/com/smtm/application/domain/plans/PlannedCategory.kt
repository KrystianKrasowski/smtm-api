package com.smtm.application.domain.plans

import com.smtm.application.domain.categories.Category
import javax.money.MonetaryAmount

data class PlannedCategory(
    val category: Category,
    val value: MonetaryAmount
)
