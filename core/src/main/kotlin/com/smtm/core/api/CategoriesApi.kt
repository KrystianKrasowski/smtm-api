package com.smtm.core.api

import arrow.core.Either
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.domain.categories.Category
import com.smtm.core.service.CategoriesService
import com.smtm.core.spi.CategoriesRepository

interface CategoriesApi {

    fun getAll(ownerId: OwnerId): Either<CategoriesProblem, Categories>

    fun save(category: Category, ownerId: OwnerId): Either<CategoriesProblem, Categories>

    fun delete(id: Long, ownerId: OwnerId): Either<CategoriesProblem, Categories>

    companion object {

        fun create(repository: CategoriesRepository): CategoriesApi = CategoriesService(repository)
    }
}
