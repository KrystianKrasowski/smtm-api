package com.smtm.application.spi

import arrow.core.Either
import arrow.core.right
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.versionOf

class FakeCategoriesRepository : CategoriesRepository {

    var version = versionOf(0)
    var list = emptyList<Category>()
    var nextCategoryId = NumericId.of(1L)
    var savedCategories: Categories? = null

    override fun getCategories(ownerId: OwnerId): Either<CategoriesProblem, Categories> {
        return Categories.fetched(ownerId, version, list).right()
    }

    override fun save(categories: Categories): Either<CategoriesProblem, Categories> {
        return categories
            .copy(current = categories.current.mergeWith(categories.toSave.applyIds()) - categories.toDelete.toSet())
            .also { savedCategories = it }
            .right()
    }

    private fun List<Category>.applyIds(): List<Category> {
        return map { category ->
            lazy { nextCategoryId++ }
                .takeIf { category.id.isUnsettled() }
                ?.value
                ?.let { category.copy(id = it) }
                ?: category
        }
    }

    private fun List<Category>.mergeWith(another: List<Category>): List<Category> {
        return map { another.findById(it.id.value) ?: it } + another.filter { findById(it.id.value) == null }
    }

    private fun List<Category>.findById(id: Long?) = firstOrNull { it.id.value == id }
}