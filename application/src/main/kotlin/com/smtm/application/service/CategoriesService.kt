package com.smtm.application.service

import arrow.core.Either
import arrow.core.flatMap
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import com.smtm.application.repository.CategoriesRepository

class CategoriesService(private val repository: CategoriesRepository) {

    fun getAll(ownerId: OwnerId): Either<CategoriesProblem, List<Category>> {
        return repository
            .getCategories(ownerId)
            .map { it.list }
    }

    fun create(category: Category, ownerId: OwnerId): Either<CategoriesProblem, Category> {
        return repository
            .getCategories(ownerId)
            .flatMap { it.add(category) }
            .flatMap { repository.save(it) }
            .map { it.first() }
    }
}