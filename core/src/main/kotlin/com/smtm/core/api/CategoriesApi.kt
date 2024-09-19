package com.smtm.core.api

import arrow.core.Either
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.service.CategoriesService
import com.smtm.core.spi.CategoriesRepository

interface CategoriesApi {

    fun getAll(owner: OwnerId): Either<CategoriesProblem, Categories>

    companion object {

        fun of(repository: CategoriesRepository): CategoriesApi =
            CategoriesService(repository)
    }
}
