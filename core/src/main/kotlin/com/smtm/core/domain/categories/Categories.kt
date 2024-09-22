package com.smtm.core.domain.categories

import arrow.core.Either
import arrow.core.NonEmptySet
import arrow.core.left
import arrow.core.right
import arrow.core.toNonEmptySetOrNull
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.Version
import com.smtm.core.domain.Violation

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
            .map { actual.toMutableList().apply { add(it) } }
            .map { copy(actual = it.toList()) }
            .mapLeft { CategoriesProblem.validationError(it) }

    private fun Category.validate(block: CategoryValidator.() -> Unit): Either<NonEmptySet<Violation>, Category> =
        CategoryValidator(this@Categories, this)
            .apply(block)
            .getViolations()
            .toNonEmptySetOrNull()
            ?.left()
            ?: this.right()

    companion object {

        fun empty(ownerId: OwnerId): Categories =
            Categories(ownerId, Version.of(0), emptyList())
    }
}

private class CategoryValidator(private val categories: Categories, private val category: Category) {

    private val violations = mutableListOf<Violation>()

    fun hasUniqueName(): Violation? =
        Violation.nonUnique("name")
            .takeIf {
                categories.any { it.name == category.name }
            }
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

private fun Regex.extractIllegalCharactersFrom(text: String) = replace(text, "")
    .toCharArray()
    .distinct()
    .toCharArray()
