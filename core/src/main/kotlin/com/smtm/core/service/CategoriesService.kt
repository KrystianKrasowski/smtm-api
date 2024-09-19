package com.smtm.core.service

import arrow.core.Either
import com.smtm.core.api.CategoriesApi
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.spi.CategoriesRepository

internal class CategoriesService(
    private val categoriesRepository: CategoriesRepository
) : CategoriesApi {

    override fun getAll(owner: OwnerId): Either<CategoriesProblem, Categories> =
        categoriesRepository.getCategories(owner)
}
