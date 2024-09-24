package com.smtm.core.domain.plans

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.categories.Category
import javax.money.MonetaryAmount
import java.time.LocalDate

data class Plan(
    val entries: List<Entry>,
    private val header: PlanHeader
) {

    val id: EntityId = header.id
    val name: String = header.name
    val start: LocalDate = header.period.start
    val end: LocalDate = header.period.endInclusive

    data class Entry(
        val category: Category,
        val value: MonetaryAmount
    )

    companion object {

        fun of(header: PlanHeader, entries: List<Entry>): Plan =
            Plan(entries, header)

        fun entry(category: Category, value: MonetaryAmount): Entry =
            Entry(category, value)
    }
}
