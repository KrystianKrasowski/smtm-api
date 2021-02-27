package com.smtm.application.transactions.categories

import com.smtm.application.transactions.categories.v1.*

class FakeCategoriesRegister : CategoriesRegister {

    var declinedCategory: AcceptingCategory? = null

    override fun accept(category: Category): AcceptingCategory = declinedCategory
        ?: category
            .copy(id = 1)
            .let { acceptedCategoryOf(it) }
}
