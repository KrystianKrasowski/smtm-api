package com.smtm.core.spi

import arrow.core.Either
import arrow.core.right
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.Version
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.domain.categories.Category

class CategoriesTestRepository : CategoriesRepository {

    var categoryList: List<Category> = listOf()

    private val categories: Categories
        get() = Categories(
            id = OwnerId.of("owner-john-doe"),
            version = Version.of(0),
            actual = categoryList
        )

    override fun getCategories(): Either<CategoriesProblem, Categories> {
        return categories.right()
    }

    override fun save(categories: Categories): Either<CategoriesProblem, Categories> {
        return categories.right()
    }
}
