package com.smtm.application.service

import arrow.core.Either
import arrow.core.flatMap
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import com.smtm.application.repository.CategoriesRepository

class CategoriesService(private val repository: CategoriesRepository) {

    fun getAll(ownerId: OwnerId): Either<CategoriesProblem, List<Category>> {
        return repository
            .getCategories(ownerId)
            .map { it.list }
    }

    fun save(category: Category, ownerId: OwnerId): Either<CategoriesProblem, Category> {
        return repository
            .getCategories(ownerId)
            .flatMap { it.add(category) }
            .flatMap { repository.save(it) }
            .map { it.getByName(category.name) }
    }

    fun delete(id: Long, ownerId: OwnerId): Either<CategoriesProblem, Categories> {
        return repository
            .getCategories(ownerId)
            .flatMap { it.delete(id) }
            .flatMap { repository.save(it) }
    }
}