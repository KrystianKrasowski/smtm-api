package com.smtm.core.api

import arrow.core.Either
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.domain.categories.Category
import com.smtm.core.service.CategoriesService
import com.smtm.core.spi.CategoriesRepository

interface CategoriesApi {

    fun getAll(): Either<CategoriesProblem, Collection<Category>>

    fun create(category: Category): Either<CategoriesProblem, Category>

    fun update(category: Category): Either<CategoriesProblem, Category>

    fun delete(id: EntityId): Either<CategoriesProblem, EntityId>

    companion object {

        fun of(repository: CategoriesRepository): CategoriesApi =
            CategoriesService(repository)
    }
}
