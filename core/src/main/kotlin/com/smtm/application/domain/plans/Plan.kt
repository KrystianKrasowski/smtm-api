package com.smtm.application.domain.plans

import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.right
import arrow.core.toNonEmptyListOrNull
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.Version
import com.smtm.application.domain.Violation
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.unknownViolationOf
import com.smtm.application.domain.violationPathOf
import javax.money.MonetaryAmount
import java.time.LocalDateTime

data class Plan(
    val version: Version,
    val ownerId: OwnerId,
    val definition: PlanDefinition,
    val entries: List<PlannedCategory>,
    val availableCategories: List<Category>
) {

    val id: NumericId
        get() = definition.id

    val name: String
        get() = definition.name

    val start: LocalDateTime
        get() = definition.period.start

    val end: LocalDateTime
        get() = definition.period.endInclusive

    val settled: Boolean =
        definition.settled

    val notEmpty: Boolean =
        entries.isNotEmpty()

    fun define(definition: PlanDefinition): Either<PlansProblem, Plan> =
        definition
            .validate()
            .map { copy(definition = it) }

    fun replace(categories: List<PlannedCategory>): Either<PlansProblem, Plan> =
        categories
            .squashDuplicates()
            .let { PlannedCategoriesValidator(it, availableCategories) }
            .validate()
            .mapLeft { PlansProblem.Violations(it) }
            .map { copy(entries = it) }

    fun getValueOf(category: String): MonetaryAmount? =
        entries
            .find { it.category.name == category }
            ?.value

    private fun List<PlannedCategory>.squashDuplicates(): List<PlannedCategory> =
        groupBy { it.category }
            .map { (category, plannedCategories) ->
                plannedCategories
                    .map { it.value }
                    .reduce { acc, monetaryAmount -> acc.add(monetaryAmount) }
                    .let { PlannedCategory(category, it) }
            }

    companion object {

        fun prepare(ownerId: OwnerId, definition: PlanDefinition, availableCategories: List<Category>): Plan =
            Plan(
                version = Version.ZERO,
                ownerId = ownerId,
                definition = definition,
                entries = emptyList(),
                availableCategories = availableCategories
            )
    }
}

private class PlannedCategoriesValidator(
    private val plannedCategories: List<PlannedCategory>,
    private val availableCategories: List<Category>
) {

    fun validate(): Either<Nel<Violation>, List<PlannedCategory>> =
        plannedCategories
            .mapIndexedNotNull(this::createUnknownViolationOrNull)
            .toNonEmptyListOrNull()
            ?.left()
            ?: plannedCategories.right()

    private fun createUnknownViolationOrNull(index: Int, plannedCategory: PlannedCategory) =
        index
            .takeUnless { availableCategories.contains(plannedCategory.category) }
            ?.let { "/entries/$it/category" }
            ?.let { violationPathOf(it) }
            ?.let { unknownViolationOf(it) }
}
