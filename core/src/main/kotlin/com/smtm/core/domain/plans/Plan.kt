package com.smtm.core.domain.plans

import com.smtm.core.domain.NumericId
import com.smtm.core.domain.categories.Category
import javax.money.MonetaryAmount
import java.time.LocalDate

data class Plan(
    val entries: List<Entry>,
    private val header: PlanHeader
) {

    val id: NumericId = header.id
    val name: String = header.name
    val start: LocalDate = header.period.start
    val end: LocalDate = header.period.endInclusive

    data class Entry(
        val category: Category,
        val value: MonetaryAmount
    )
}
