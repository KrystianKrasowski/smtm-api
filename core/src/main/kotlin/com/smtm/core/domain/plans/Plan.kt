package com.smtm.core.domain.plans

import arrow.core.Either
import arrow.core.NonEmptySet
import arrow.core.left
import arrow.core.right
import arrow.core.toNonEmptySetOrNull
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Version
import com.smtm.core.domain.Violation
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.shared.extractIllegalCharactersFrom
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

    fun validate(): Either<PlansProblem, Plan> =
        PlanValidator(this)
            .validate {
                hasLegalCharacters()
                hasValidRange()
                hasUniqueEntries()
                hasKnownCategories()
            }
            .mapLeft { PlansProblem.validationErrors(it) }

    fun redefine(header: PlanHeader): Plan =
        copy(header = header)

    fun redefine(entries: List<Entry>): Plan =
        copy(entries = entries)

    data class Entry(
        val categoryId: EntityId,
        val value: MonetaryAmount
    )

    companion object {

        fun validated(
            header: PlanHeader,
            entries: List<Entry>,
            categories: List<Category>
        ): Either<PlansProblem, Plan> =
            Plan(entries, categories, Version.of(0), header).validate()

        fun entry(categoryId: EntityId, value: MonetaryAmount): Entry =
            Entry(categoryId, value)
    }
}

private class PlanValidator(private val plan: Plan) {

    private val violations: MutableList<Violation> = mutableListOf()

    fun validate(block: PlanValidator.() -> Unit): Either<NonEmptySet<Violation>, Plan> =
        apply(block)
            .let { violations.toNonEmptySetOrNull() }
            ?.left()
            ?: plan.right()

    fun hasValidRange(): Violation? =
        Violation.invalid("period")
            .takeIf { plan.start >= plan.end }
            ?.also { violations.add(it) }

    // TODO: remove duplicate
    fun hasLegalCharacters(): Violation? =
        "[\\p{IsLatin}0-9 ]+"
            .toRegex()
            .extractIllegalCharactersFrom(plan.name)
            .takeIf { it.isNotEmpty() }
            ?.let { Violation.illegalCharacters("name", it) }
            ?.also { violations.add(it) }

    fun hasUniqueEntries(): List<Violation> =
        plan.entries
            .asSequence()
            .withIndex()
            .groupBy { it.value.categoryId }
            .filter { it.value.size > 1 }
            .map { it.value.last().index }
            .map { Violation.nonUnique("entries/${it + 1}/category") }
            .toList()
            .also { violations.addAll(it) }

    fun hasKnownCategories(): List<Violation> =
        plan.entries
            .asSequence()
            .withIndex()
            .filter { indexedValue -> plan.categories.none { it.id == indexedValue.value.categoryId} }
            .map { it.index + 1 }
            .map { Violation.unknown("entries/$it/category") }
            .toList()
            .also { violations.addAll(it) }
}
