package com.smtm.transactions.categories

import com.smtm.transactions.api.AcceptingCategory
import com.smtm.transactions.api.CategoriesRegister
import com.smtm.transactions.api.Category
import com.smtm.transactions.api.declinedCategoryOf
import com.smtm.transactions.spi.CategoriesRepository

internal class CategoriesRegisterImpl(private val validator: NewCategoryValidator, private val repository: CategoriesRepository) : CategoriesRegister {

    override fun accept(category: Category): AcceptingCategory = validator.validate(category)
        .takeIf { it.isNotEmpty() }
        ?.let { declinedCategoryOf(it) }
        ?: repository.add(category)
}

fun categoriesRegisterOf(repository: CategoriesRepository): CategoriesRegister = CategoriesRegisterImpl(NewCategoryValidator(repository), repository)
