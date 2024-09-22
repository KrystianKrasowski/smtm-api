package com.smtm.application.domain.categories

import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.right
import arrow.core.toNonEmptyListOrNull
import com.smtm.application.domain.Aggregate
import com.smtm.application.domain.UserId
import com.smtm.application.domain.Version
import com.smtm.application.domain.Violation
import com.smtm.application.domain.emptyViolationOf
import com.smtm.application.domain.nonUniqueViolationOf
import com.smtm.application.domain.violationPathOf

typealias CategoriesActionResult = Either<CategoriesProblem, Categories>

data class Categories(
    override val id: UserId,
    override val version: Version,
    val categories: List<Category>
) : Aggregate<UserId> {

    val newCategories = categories.filter { it.isNew() }

    fun add(category: Category): CategoriesActionResult {
        return category
            .validate()
            .map { Categories(id, version.increment(), categories + it) }
    }

    private fun Category.validate() = CategoryValidator(categories, this)
        .validate()
        .mapLeft { CategoriesProblem.Violations(it) }
}

private class CategoryValidator(currentCategories: List<Category>, private val newCategory: Category) {

    private val currentCategoryNames = currentCategories.map { it.name }

    private val emptyNameViolation = emptyViolationOf(violationPathOf("name"))
        .takeIf { newCategory.name.isBlank() }

    private val nonUniqueViolation = nonUniqueViolationOf(violationPathOf("name"))
        .takeIf { currentCategoryNames.contains(newCategory.name) }

    fun validate(): Either<Nel<Violation>, Category> {
        return getViolationsOrNull()
            .toNonEmptyListOrNull()
            ?.left()
            ?: newCategory.right()
    }

    fun getViolationsOrNull() = listOfNotNull(
        emptyNameViolation,
        nonUniqueViolation
    )
}