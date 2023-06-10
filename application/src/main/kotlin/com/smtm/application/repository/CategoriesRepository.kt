package com.smtm.application.repository

import arrow.core.Either
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category

interface CategoriesRepository {

    fun getCategories(ownerId: OwnerId): Either<CategoriesProblem, Categories>

    fun save(categories: Categories): Either<CategoriesProblem, List<Category>>
}