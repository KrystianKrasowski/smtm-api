package com.smtm.application.transactions.categories.v1

import com.smtm.common.ConstraintViolation

interface CategoriesRegister {

    fun accept(category: Category): AcceptingCategory
}

data class Category internal constructor(val id: Long?, val name: String, val icon: String, val parent: Long?)

sealed class AcceptingCategory {

    data class Accepted internal constructor(val category: Category) : AcceptingCategory()
    data class Declined internal constructor(val violations: List<ConstraintViolation>) : AcceptingCategory()
}

fun categoryOf(id: Long?, name: String, icon: String, parent: Long? = null) = Category(id, name, icon, parent)

fun acceptedCategoryOf(category: Category) = AcceptingCategory.Accepted(category)

fun declinedCategoryOf(violations: List<ConstraintViolation>) = AcceptingCategory.Declined(violations)
