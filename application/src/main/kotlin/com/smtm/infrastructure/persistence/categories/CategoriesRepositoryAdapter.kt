package com.smtm.infrastructure.persistence.categories

import com.smtm.transactions.api.AcceptingCategory
import com.smtm.transactions.api.Category
import com.smtm.transactions.api.acceptedCategoryOf
import com.smtm.transactions.api.categoryOf
import com.smtm.transactions.spi.CategoriesRepository
import com.smtm.infrastructure.persistence.categories.Category as CategoryEntity

class CategoriesRepositoryAdapter(private val dbCategoriesRepository: DbCategoriesRepository) : CategoriesRepository {

    override fun add(category: Category): AcceptingCategory = category
        .toEntity()
        .let { dbCategoriesRepository.save(it) }
        .toCategory()
        .let { acceptedCategoryOf(it) }

    override fun hasRegisteredName(name: String): Boolean = dbCategoriesRepository.findByName(name) != null

    override fun hasRegisteredId(id: Long): Boolean = dbCategoriesRepository.findById(id).isPresent
}

private fun Category.toEntity() = CategoryEntity(id, parent, name, icon)

private fun CategoryEntity.toCategory() = categoryOf(id, name, icon, parent)
