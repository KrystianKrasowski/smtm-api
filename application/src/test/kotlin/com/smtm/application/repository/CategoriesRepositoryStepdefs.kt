package com.smtm.application.repository

import com.smtm.application.World
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.versionOf
import io.cucumber.java.en.Given

class CategoriesRepositoryStepdefs(private val world: World) {

    @Given("user categories version is {int}")
    fun `user categories version is N`(version: Int) {
        world.categoriesRepository.version = versionOf(version)
    }

    @Given("user has categories defined")
    fun `user has categories defined`(categories: List<Category>) {
        world.categoriesRepository.categoryList = categories
    }}