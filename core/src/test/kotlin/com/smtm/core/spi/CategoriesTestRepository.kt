package com.smtm.core.spi

import arrow.core.Either
import arrow.core.right
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Version
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.tags.Tags
import com.smtm.core.domain.tags.TagsProblem

class CategoriesTestRepository : CategoriesRepository {

    var categoryList: List<Category> = listOf()

    private val categories: Tags<Category>
        get() = Tags(
            id = EntityId.of("owner-john-doe"),
            version = Version.of(0),
            tagsCollection = categoryList
        )

    override fun getCategories(): Either<TagsProblem, Tags<Category>> {
        return categories.right()
    }

    override fun save(categories: Tags<Category>): Either<TagsProblem, Tags<Category>> {
        return categories.right()
    }
}
