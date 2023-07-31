package com.smtm.application.domain.plans

import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.right
import arrow.core.toNonEmptyListOrNull
import com.smtm.application.domain.Aggregate
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.Version
import com.smtm.application.domain.Violation
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.unknownViolationOf
import com.smtm.application.domain.violationPathOf
import java.time.LocalDateTime

data class Plan(
    override val version: Version,
    val ownerId: OwnerId,
    val definition: PlanDefinition,
    val entries: List<PlannedCategory>,
    val newEntries: List<PlannedCategory>,
    private val availableCategories: List<Category>
) : Aggregate<PlanId> {

    override val id: PlanId
        get() = definition.id

    val name: String
        get() = definition.name

    val start: LocalDateTime
        get() = definition.period.start

    val end: LocalDateTime
        get() = definition.period.endInclusive

    fun define(newPlan: NewPlan): Either<PlansProblem, Plan> {
        return newPlan
            .validate()
            .map {
                copy(
                    version = version.increment(),
                    definition = it.definition,
                    newEntries = it.createPlannedCategoriesWith(availableCategories)
                )
            }
    }

    private fun NewPlan.validate() = PlanValidator(this, availableCategories.mapNotNull { it.id })
        .validate()
        .mapLeft { PlansProblem.Violations(it) }

    companion object {

        fun prepared(availableCategories: List<Category>, ownerId: OwnerId) = Plan(
            version = Version.ZERO,
            ownerId = ownerId,
            definition = PlanDefinition.EMPTY,
            entries = emptyList(),
            newEntries = emptyList(),
            availableCategories = availableCategories
        )
    }
}

private class PlanValidator(private val plan: NewPlan, private val availableCategories: List<Long>) {

    fun validate(): Either<Nel<Violation>, NewPlan> {
        return plan
            .entries
            .map { it.categoryId }
            .mapIndexedNotNull(this::createUnknownViolationOrNull)
            .toNonEmptyListOrNull()
            ?.left()
            ?: plan.right()
    }

    private fun createUnknownViolationOrNull(index: Int, category: Long) = index
        .takeUnless { availableCategories.contains(category) }
        ?.let { "/entries/$it/category" }
        ?.let { violationPathOf(it) }
        ?.let { unknownViolationOf(it) }
}