package com.smtm.application.categories.v1

data class Category internal constructor(val id: Long, val name: String, val icon: String)

fun categoryOf(id: Long, name: String, icon: String) = Category(id, name, icon)
