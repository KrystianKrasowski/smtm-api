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
import com.smtm.application.domain.illegalCharactersViolationOf
import com.smtm.application.domain.nonUniqueViolationOf
import com.smtm.application.domain.violationPathOf

typealias CategoriesActionResult = Either<CategoriesProblem, Categories>

data class Categories(
    override val id: OwnerId,
    override val version: Version,
    val current: List<Category>,
    val toSave: List<Category>,
    val toDelete: List<Category>
) : Aggregate<OwnerId> {

    fun store(category: Category): CategoriesActionResult = category
        .takeUnless { contains(category) }
        ?.validate()
        ?.map { copy(toSave = listOf(it), version = version.increment()) }
        ?: this.right()

    fun getByName(name: String): Category = current.first { it.name == name }

    fun getById(id: Long): Category = current.first { it.id == id }

    fun delete(id: Long): CategoriesActionResult = findById(id)
        ?.let { copy(version = version.increment(), toDelete = listOf(it)) }
        ?.right()
        ?: CategoriesProblem.Unknown.left()

    private fun contains(category: Category) = current
        .any { it.id == category.id && it.name == category.name && it.icon == category.icon }

    private fun Category.validate() = CategoryValidator(current, this)
        .validate()
        .mapLeft { CategoriesProblem.Violations(it) }

    private fun findById(id: Long) = current.firstOrNull { it.id == id }

    companion object {

        fun fetched(id: OwnerId, version: Version, list: List<Category>) = Categories(
            id = id,
            version = version,
            current = list,
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
        .takeIf { category.id == null && currentCategoryNames.contains(category.name) }

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