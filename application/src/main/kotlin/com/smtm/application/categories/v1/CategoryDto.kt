package com.smtm.application.categories.v1

data class CategoryDto(val id: Long?, val name: String, val icon: String) {

    companion object {

        const val MediaTypeValue = "application/smtm.category.v1+json"
    }
}

fun categoryDtoOf(category: Category) = categoryDtoOf(category.name, category.icon, category.id)

fun categoryDtoOf(name: String, icon: String, id: Long? = null) = CategoryDto(id, name, icon)
