package com.smtm.application.transactions.categories.v1

interface CategoriesRegister {

    fun accept(category: Category): AcceptingCategory
}

data class Category internal constructor(val id: Long?, val name: String, val icon: String, val parent: Long?)

sealed class AcceptingCategory {

    data class Accepted internal constructor(val category: Category) : AcceptingCategory()
    data class Declined internal constructor(val violations: List<ConstraintViolation>) : AcceptingCategory()
}

data class ConstraintViolation internal constructor(val property: String, val message: Message)

data class Message internal constructor(val pattern: String, val parameters: Map<String, String>)

fun categoryOf(id: Long?, name: String, icon: String, parent: Long? = null) = Category(id, name, icon, parent)

fun acceptedCategoryOf(category: Category) = AcceptingCategory.Accepted(category)

fun declinedCategoryOf(violations: List<ConstraintViolation>) = AcceptingCategory.Declined(violations)

fun constraintViolationOf(property: String, message: Message) = ConstraintViolation(property, message)

fun messageOf(pattern: String, parameters: Map<String, String> = emptyMap()) = Message(pattern, parameters)
