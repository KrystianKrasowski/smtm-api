package com.smtm.core.spi

import arrow.core.Either
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.tags.Tags
import com.smtm.core.domain.tags.TagsProblem

interface CategoriesRepository {

    fun getCategories(): Either<TagsProblem, Tags<Category>>

    fun save(categories: Tags<Category>): Either<TagsProblem, Tags<Category>>
}
