package com.smtm.application.spring.conversions

import com.smtm.api.HalCollection
import com.smtm.api.LinkFactory
import com.smtm.api.ResourcePaths
import com.smtm.api.v1.CategoryDto
import com.smtm.api.v1.CategoryResource
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.Category

object CategoriesConversions {

    fun Categories.toHalCollection(linkFactory: LinkFactory): HalCollection =
        HalCollection(
            links = mapOf(
                "self" to linkFactory.create(ResourcePaths.CATEGORIES)
            ),
            count = size,
            total = size,
            embedded = mapOf(
                "categories" to map { it.toResource(linkFactory) }
            )
        )

    fun Category.toResource(linkFactory: LinkFactory): CategoryResource =
        CategoryResource(
            links = mapOf(
                "self" to linkFactory.create("${ResourcePaths.CATEGORIES}/$id")
            ),
            id = id.toString(),
            category = CategoryDto(
                name = name,
                icon = icon.name
            )
        )
}
