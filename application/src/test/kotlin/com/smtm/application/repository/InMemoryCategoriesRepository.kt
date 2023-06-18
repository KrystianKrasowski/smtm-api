package com.smtm.application.repository

import arrow.core.Either
import arrow.core.right
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.versionOf

class InMemoryCategoriesRepository : CategoriesRepository {

    var version = versionOf(0)
    var categoryList = emptyList<Category>()
    var categories: Categories? = null

    override fun getCategories(ownerId: OwnerId): Either<CategoriesProblem, Categories> {
        return Categories(ownerId, version, categoryList).right()
    }

    override fun save(categories: Categories): Either<CategoriesProblem, Categories> {
        this.categories = categories
        val categoriesWithAppliedIds = categories.applyIds()
        this.version = categoriesWithAppliedIds.version
        this.categoryList = categoriesWithAppliedIds.list
        return categoriesWithAppliedIds.right()
    }

    private fun Categories.applyIds(): Categories {
        val categoriesWithId: MutableList<Category> = list.filter { it.id != null }.toMutableList()
        list
            .filter { it.id == null }
            .forEach { categoriesWithId.add(it.copy(id = categoriesWithId.getMaxId() + 1)) }
        return copy(list = categoriesWithId.toList())
    }

    private fun Iterable<Category>.getMaxId() = this
        .filter { it.id != null }
        .maxBy { it.id!! }
        .id!!
}