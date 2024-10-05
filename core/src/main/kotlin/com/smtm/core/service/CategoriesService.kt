package com.smtm.core.service

import arrow.core.Either
import arrow.core.flatMap
import com.smtm.core.api.CategoriesApi
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.domain.categories.Category
import com.smtm.core.spi.CategoriesRepository

internal class CategoriesService(
    private val categoriesRepository: CategoriesRepository
) : CategoriesApi {

    override fun getAll(): Either<CategoriesProblem, Collection<Category>> =
        categoriesRepository
            .getCategories()
            .map { it.toList() }
            .mapLeft { CategoriesProblem.from(it) }

    override fun create(category: Category): Either<CategoriesProblem, Category> =
        categoriesRepository
            .getCategories()
            .flatMap { it.add(category) }
            .flatMap { categoriesRepository.save(it) }
            .flatMap { it.getById(category.id) }
            .mapLeft { CategoriesProblem.from(it) }

    override fun update(category: Category): Either<CategoriesProblem, Category> =
        categoriesRepository
            .getCategories()
            .flatMap { it.replace(category) }
            .flatMap { categoriesRepository.save(it) }
            .flatMap { it.getById(category.id) }
            .mapLeft { CategoriesProblem.from(it) }

    override fun delete(id: EntityId): Either<CategoriesProblem, EntityId> =
        categoriesRepository
            .getCategories()
            .flatMap { it.delete(id) }
            .flatMap { categoriesRepository.save(it) }
            .map { id }
            .mapLeft { CategoriesProblem.from(it) }
}
