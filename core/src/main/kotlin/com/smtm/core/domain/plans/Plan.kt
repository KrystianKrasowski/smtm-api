package com.smtm.core.domain.plans

import arrow.core.Either
import arrow.core.right
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Version
import com.smtm.core.domain.categories.Category
import javax.money.MonetaryAmount
import java.time.LocalDate

data class Plan(
    val entries: List<Entry>,
    val categories: List<Category>,
    val version: Version,
    private val header: PlanHeader,
) {

    val id: EntityId = header.id
    val name: String = header.name
    val start: LocalDate = header.period.start
    val end: LocalDate = header.period.endInclusive

    data class Entry(
        val categoryId: EntityId,
        val value: MonetaryAmount
    )

    companion object {

        fun newValidated(
            header: PlanHeader,
            entries: List<Entry>,
            categories: List<Category>
        ): Either<PlansProblem, Plan> =
            Plan(entries, categories, Version.of(0), header).right()

        fun entry(categoryId: EntityId, value: MonetaryAmount): Entry =
            Entry(categoryId, value)
    }
}
