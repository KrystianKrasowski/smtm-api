package com.smtm.core.domain.categories

import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.right
import arrow.core.toNonEmptyListOrNull
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.Version
import com.smtm.core.domain.Violation
import com.smtm.core.domain.emptyViolationOf
import com.smtm.core.domain.illegalCharactersViolationOf
import com.smtm.core.domain.nonUniqueViolationOf
import com.smtm.core.domain.violationPathOf

typealias CategoriesActionResult = Either<CategoriesProblem, Categories>

data class Categories(
    val id: OwnerId,
    val version: Version,
    val current: List<Category>,
    val toSave: List<Category>,
    val toDelete: List<Category>
) {

    fun store(category: Category): CategoriesActionResult = category
        .takeUnless { contains(category) }
        ?.validate()
        ?.map { copy(toSave = listOf(it), version = version.increment()) }
        ?: this.right()

    fun getByName(name: String): Category = current.first { it.name == name }

    fun findByName(name: String): Category? =
        current.firstOrNull { it.name == name }

    fun getById(id: NumericId): Category =
        current.first { it.id == id }

    fun delete(id: NumericId): CategoriesActionResult =
        findById(id)
            ?.let { copy(version = version.increment(), toDelete = listOf(it)) }
            ?.right()
            ?: CategoriesProblem.Unknown.left()

    private fun contains(category: Category) = current
        .any { it.id == category.id && it.name == category.name && it.icon == category.icon }

    private fun Category.validate() = CategoryValidator(current, this)
        .validate()
        .mapLeft { CategoriesProblem.Violations(it) }

    private fun findById(id: NumericId) =
        current.firstOrNull { it.id == id }

    companion object {

        fun fetched(id: OwnerId, version: Version, list: List<Category>) = Categories(
            id = id,
            version = version,
            current = list,
            toSave = emptyList(),
            toDelete = emptyList()
        )

        fun empty(id: OwnerId) = Categories(
            id = id,
            version = Version.ZERO,
            current = emptyList(),
            toSave = emptyList(),
            toDelete = emptyList()
        )
    }
}

private class CategoryValidator(currentCategories: List<Category>, private val category: Category) {

    private val currentCategoryNames = currentCategories.map { it.name }

    private val emptyNameViolation = emptyViolationOf(violationPathOf("name"))
        .takeIf { category.name.isBlank() }

    private val nonUniqueViolation = nonUniqueViolationOf(violationPathOf("name"))
        .takeIf { category.id == NumericId.UNSETTLED && currentCategoryNames.contains(category.name) }

    private val illegalCharactersViolation = NAME_REGEX
        .toRegex()
        .extractIllegalCharactersFrom(category.name)
        .takeIf { it.isNotEmpty() }
        ?.let { illegalCharactersViolationOf(violationPathOf("name"), it) }

    fun validate(): Either<Nel<Violation>, Category> {
        return getViolations()
            .toNonEmptyListOrNull()
            ?.left()
            ?: category.right()
    }

    fun getViolations() = listOfNotNull(
        emptyNameViolation,
        nonUniqueViolation,
        illegalCharactersViolation
    )

    companion object {

        private const val NAME_REGEX = "[\\p{IsLatin}0-9 ]+"
    }
}

private fun Regex.extractIllegalCharactersFrom(text: String) = replace(text, "")
    .toCharArray()
    .distinct()
    .toCharArray()
