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

    override fun getCategories(ownerId: OwnerId): Either<CategoriesProblem, Categories> {
        return Categories(ownerId, version, categoryList).right()
    }

    override fun save(categories: Categories): Either<CategoriesProblem, Categories> {
        this.version = categories.version
        this.categoryList = categories.list
        return categories.right()
    }
}