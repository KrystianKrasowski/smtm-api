package com.smtm.core.service

import arrow.core.Either
import arrow.core.flatMap
import com.smtm.core.api.CategoriesApi
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.domain.categories.Category
import com.smtm.core.spi.CategoriesRepository

internal class CategoriesService(
    private val categoriesRepository: CategoriesRepository
) : CategoriesApi {

    override fun getAll(): Either<CategoriesProblem, Categories> =
        categoriesRepository.getCategories()

    override fun create(category: Category): Either<CategoriesProblem, Categories> =
        categoriesRepository
            .getCategories()
            .flatMap { it.add(category) }
            .flatMap { categoriesRepository.save(it) }

    override fun update(category: Category): Either<CategoriesProblem, Categories> =
        categoriesRepository
            .getCategories()
            .flatMap { it.replace(category) }
            .flatMap { categoriesRepository.save(it) }
}
