package com.smtm.application.spring.infrastructure.storage

import arrow.core.Either
import arrow.core.right
import com.smtm.application.domain.Icon
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.categories.categoryOf
import com.smtm.application.domain.userIdOf
import com.smtm.application.domain.versionOf
import com.smtm.application.repository.CategoriesRepository

class InMemoryCategoriesRepository : CategoriesRepository {

    private val categoryList = mutableListOf(
        categoryOf(1, "Rent", Icon.FOLDER),
        categoryOf(2, "Savings", Icon.FOLDER),
        categoryOf(3, "Services", Icon.FOLDER),
    )

    private val categories: Categories
        get() = Categories(userIdOf(1), versionOf(1), categoryList.toList())

    override fun getCategories(): Either<CategoriesProblem, Categories> {
        return categories.right()
    }

    override fun save(categories: Categories): Either<CategoriesProblem, List<Category>> {
        val newCategories = categories.newCategories
            .mapIndexed { index, category -> category.copy(id = index + categories.list.size.toLong()) }

        categoryList += newCategories

        return newCategories.right()
//        return CategoriesProblem.Other("Something went wrong").left()
    }
}