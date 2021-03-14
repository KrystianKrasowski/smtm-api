package com.smtm.transactions.spi

import com.smtm.transactions.api.AcceptingCategory
import com.smtm.transactions.api.Category
import com.smtm.transactions.api.acceptedCategoryOf

class FakeCategoriesRepository : CategoriesRepository {

    val registeredCategories = mutableListOf<Category>()

    override fun add(category: Category): AcceptingCategory = category
        .copy(id = generateId())
        .also { registeredCategories.add(it) }
        .let { acceptedCategoryOf(it) }

    override fun hasRegisteredName(name: String) = registeredCategories.any { it.name == name }

    private fun generateId() = registeredCategories
        .maxOf { (it.id ?: 0) }
        .let { it + 1 }
}
