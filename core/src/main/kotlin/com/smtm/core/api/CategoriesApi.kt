package com.smtm.core.api

import arrow.core.Either
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.domain.categories.Category
import com.smtm.core.service.CategoriesService
import com.smtm.core.spi.CategoriesRepository

interface CategoriesApi {

    fun getAll(): Either<CategoriesProblem, Categories>

    fun create(category: Category): Either<CategoriesProblem, Categories>

    companion object {

        fun of(repository: CategoriesRepository): CategoriesApi =
            CategoriesService(repository)
    }
}
