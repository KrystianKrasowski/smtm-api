package com.smtm.infrastructure.persistence.categories

import org.springframework.data.repository.CrudRepository

interface DbCategoriesRepository : CrudRepository<Category, Long> {

    fun findByName(name: String): Category?
}
