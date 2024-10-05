package com.smtm.core.domain.categories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.Version
import com.smtm.core.domain.tags.TagValidator

data class Categories(
    val id: OwnerId,
    val version: Version,
    private val categoryList: List<Category>
) : Iterable<Category> {

    override fun iterator(): Iterator<Category> =
        categoryList.iterator()

    fun getById(id: EntityId): Either<CategoriesProblem, Category> =
        firstOrNull { it.id == id }
            ?.right()
            ?: CategoriesProblem.illegalState("Category of id: $id does not exist").left()

    fun getByName(name: String): Category =
        first { it.name == name }

    fun add(category: Category): Either<CategoriesProblem, Categories> =
        validate(category)
            .map { upsertCategory(it) }

    fun replace(category: Category): Either<CategoriesProblem, Categories> =
        category
            .takeIf { hasCategoryWithId(it.id) }
            ?.let { validate(it) }
            ?.map { upsertCategory(it) }
            ?: CategoriesProblem.unknown(category.id).left()

    fun delete(categoryId: EntityId): Either<CategoriesProblem, Categories> =
        categoryId
            .takeIf { hasCategoryWithId(it) }
            ?.let { deleteCategoryById(it) }
            ?.right()
            ?: CategoriesProblem.unknown(categoryId).left()

    private fun validate(category: Category): Either<CategoriesProblem, Category> =
        TagValidator(category, categoryList)
            .validate {
                hasUniqueName()
                hasNotEmptyName()
                hasLegalCharacters()
            }
            .mapLeft { CategoriesProblem.validationError(it) }

    private fun hasCategoryWithId(id: EntityId): Boolean =
        any { it.id == id }

    private fun upsertCategory(category: Category): Categories =
        categoryList.toMutableList()
            .apply { removeIf { it.id == category.id } }
            .apply { add(category) }
            .let { copy(categoryList = it.toList()) }

    private fun deleteCategoryById(categoryId: EntityId): Categories =
        categoryList.toMutableList()
            .apply { removeIf { it.id == categoryId } }
            .let { copy(categoryList = it.toList()) }

    companion object {

        fun empty(ownerId: OwnerId): Categories =
            Categories(ownerId, Version.of(0), emptyList())
    }
}
