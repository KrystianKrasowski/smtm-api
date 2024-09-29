package com.smtm.core.domain.categories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.Version
import com.smtm.core.domain.Violation
import com.smtm.core.domain.shared.extractIllegalCharactersFrom

data class Categories(
    val id: OwnerId,
    val version: Version,
    private val actual: List<Category>
) : Iterable<Category> {

    val size: Int =
        actual.size

    override fun iterator(): Iterator<Category> {
        return actual.iterator()
    }

    fun getByName(name: String): Category =
        first { it.name == name }

    fun add(category: Category): Either<CategoriesProblem, Categories> =
        category
            .validate {
                hasUniqueName()
                hasNotEmptyName()
                hasLegalCharacters()
            }
            .map { upsertCategory(it) }

    fun replace(category: Category): Either<CategoriesProblem, Categories> =
        category
            .takeIf { hasCategoryWithId(it.id) }
            ?.validate {
                hasNotEmptyName()
                hasLegalCharacters()
            }
            ?.map { upsertCategory(it) }
            ?: CategoriesProblem.unknown(category.id).left()

    fun delete(categoryId: EntityId): Either<CategoriesProblem, Categories> =
        categoryId
            .takeIf { hasCategoryWithId(it) }
            ?.let { deleteCategoryById(it) }
            ?.right()
            ?: CategoriesProblem.unknown(categoryId).left()

    private fun Category.validate(block: CategoryValidator.() -> Unit): Either<CategoriesProblem, Category> =
        CategoryValidator(this@Categories, this)
            .apply(block)
            .getViolations()
            .takeIf { it.isNotEmpty() }
            ?.let { CategoriesProblem.validationError(it) }
            ?.left()
            ?: this.right()

    private fun hasCategoryWithId(id: EntityId): Boolean =
        any { it.id == id }

    private fun upsertCategory(category: Category): Categories =
        actual.toMutableList()
            .apply { removeIf { it.id == category.id } }
            .apply { add(category) }
            .let { copy(actual = it.toList()) }

    private fun deleteCategoryById(categoryId: EntityId): Categories =
        actual.toMutableList()
            .apply { removeIf { it.id == categoryId } }
            .let { copy(actual = it.toList()) }

    companion object {

        fun empty(ownerId: OwnerId): Categories =
            Categories(ownerId, Version.of(0), emptyList())
    }
}

private class CategoryValidator(private val categories: Categories, private val category: Category) {

    private val violations = mutableListOf<Violation>()

    fun hasUniqueName(): Violation? =
        Violation.nonUnique("name")
            .takeIf { categories.any { it.name == category.name } }
            ?.also { violations.add(it) }

    fun hasNotEmptyName(): Violation? =
        Violation.empty("name")
            .takeIf { category.name.isBlank() }
            ?.also { violations.add(it) }

    fun hasLegalCharacters(): Violation? =
        "[\\p{IsLatin}0-9 ]+"
            .toRegex()
            .extractIllegalCharactersFrom(category.name)
            .takeIf { it.isNotEmpty() }
            ?.let { Violation.illegalCharacters("name", it) }
            ?.also { violations.add(it) }


    fun getViolations(): Collection<Violation> =
        violations.toSet()
}
