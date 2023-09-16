package com.smtm.application.api

import arrow.core.Either
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import com.smtm.application.service.CategoriesService
import com.smtm.application.spi.CategoriesRepository

interface CategoriesApi {

    fun getAll(ownerId: OwnerId): Either<CategoriesProblem, Categories>

    fun save(category: Category, ownerId: OwnerId): Either<CategoriesProblem, Categories>

    fun delete(id: Long, ownerId: OwnerId): Either<CategoriesProblem, Categories>

    companion object {

        fun create(repository: CategoriesRepository): CategoriesApi = CategoriesService(repository)
    }
}
