package com.smtm.application.domain.categories

import com.smtm.application.domain.Icon

data class Category(val id: Long?, val name: String, val icon: Icon, val status: Status) {

    enum class Status {
        EXISTING, NEW, DELETED
    }
}

fun categoryOf(id: Long?, name: String, icon: Icon, status: Category.Status) = Category(id, name, icon, status)

fun newCategoryOf(name: String, icon: Icon) = categoryOf(
    id = null,
    name = name,
    icon = icon,
    status = Category.Status.NEW
)

fun existingCategoryOf(id: Long, name: String, icon: Icon) = categoryOf(
    id = id,
    name = name,
    icon = icon,
    status = Category.Status.EXISTING
)

fun deletedCategoryOf(id: Long, name: String, icon: Icon) = categoryOf(
    id = id,
    name = name,
    icon = icon,
    status = Category.Status.DELETED
)