package com.smtm.core.spi

import arrow.core.Either
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.CategoriesProblem

interface CategoriesRepository {

    fun getCategories(): Either<CategoriesProblem, Categories>

    fun save(categories: Categories): Either<CategoriesProblem, Categories>
}
