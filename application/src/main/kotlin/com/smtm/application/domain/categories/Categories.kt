package com.smtm.application.domain.categories

import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.right
import arrow.core.toNonEmptyListOrNull
import com.smtm.application.domain.Aggregate
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.Version
import com.smtm.application.domain.Violation
import com.smtm.application.domain.emptyViolationOf
import com.smtm.application.domain.nonUniqueViolationOf
import com.smtm.application.domain.violationPathOf

typealias CategoriesActionResult = Either<CategoriesProblem, Categories>

data class Categories(
    override val id: OwnerId,
    override val version: Version,
    val list: List<Category>
) : Aggregate<OwnerId> {

    fun add(category: Category): CategoriesActionResult {
        return category
            .validate()
            .map { Categories(id, version.increment(), list + it) }
    }

    fun getByName(name: String): Category = list.first { it.name == name }

    fun isFirstVersion(): Boolean = version.value == 1L

    private fun Category.validate() = CategoryValidator(list, this)
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