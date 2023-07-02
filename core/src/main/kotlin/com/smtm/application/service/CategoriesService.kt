package com.smtm.application.service

import arrow.core.Either
import arrow.core.flatMap
import com.smtm.application.api.CategoriesApi
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import com.smtm.application.spi.CategoriesRepository

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
            .flatMap { it.delete(id) }
            .flatMap { repository.save(it) }
    }
}