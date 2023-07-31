package com.smtm.application.domain.categories

import com.smtm.application.domain.Icon

data class Category(val id: Long?, val name: String, val icon: Icon) {

    companion object {

        fun of(id: Long, name: String, icon: Icon) = Category(id, name, icon)
    }
}

fun categoryOf(id: Long?, name: String, icon: Icon) = Category(id, name, icon)

fun newCategoryOf(name: String, icon: Icon) = categoryOf(
    id = null,
    name = name,
    icon = icon,
)

fun existingCategoryOf(id: Long, name: String, icon: Icon) = categoryOf(
    id = id,
    name = name,
    icon = icon,
)