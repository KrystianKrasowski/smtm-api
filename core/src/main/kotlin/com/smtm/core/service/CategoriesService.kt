package com.smtm.core.service

import arrow.core.Either
import arrow.core.flatMap
import com.smtm.core.api.CategoriesApi
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.domain.categories.Category
import com.smtm.core.spi.CategoriesRepository

internal class CategoriesService(private val repository: CategoriesRepository): CategoriesApi {

    override fun getAll(ownerId: OwnerId): Either<CategoriesProblem, Categories> {
        return repository.getCategories(ownerId)
    }

    override fun save(category: Category, ownerId: OwnerId): Either<CategoriesProblem, Categories> {
        return repository
            .getCategories(ownerId)
            .flatMap { it.store(category) }
            .flatMap { repository.save(it) }
    }

    override fun delete(id: Long, ownerId: OwnerId): Either<CategoriesProblem, Categories> {
        return repository
            .getCategories(ownerId)
            .flatMap { it.delete(NumericId.of(id)) }
            .flatMap { repository.save(it) }
    }
}
