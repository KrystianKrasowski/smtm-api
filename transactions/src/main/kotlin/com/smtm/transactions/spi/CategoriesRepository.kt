package com.smtm.transactions.spi

import com.smtm.transactions.api.AcceptingCategory
import com.smtm.transactions.api.Category

interface CategoriesRepository {

    fun add(category: Category): AcceptingCategory

    fun hasRegisteredName(name: String): Boolean
}
