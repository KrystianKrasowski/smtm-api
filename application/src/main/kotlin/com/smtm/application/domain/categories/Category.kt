package com.smtm.application.domain.categories

import com.smtm.application.domain.Icon

data class Category(val id: Long?, val name: String, val icon: Icon) {

    fun isNew() = id == null
}

fun newCategoryOf(name: String, icon: Icon) = Category(
    id = null,
    name = name,
    icon = icon
)

fun categoryOf(id: Long, name: String, icon: Icon) = Category(
    id = id,
    name = name,
    icon = icon
)