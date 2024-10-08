package com.smtm.application.spring.conversions

import com.smtm.api.LinkFactory
import com.smtm.api.ResourcePaths
import com.smtm.api.v1.CategoriesCollection
import com.smtm.api.v1.CategoryDto
import com.smtm.api.v1.CategoryResource
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon
import com.smtm.core.domain.categories.Category

internal object Categories {

    fun Collection<Category>.toHalCollection(linkFactory: LinkFactory): CategoriesCollection =
        CategoriesCollection(
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
            id = id.asString(),
            category = CategoryDto(
                name = name,
                icon = icon.name
            )
        )

    fun CategoryDto.toDomain(id: String? = null): Category =
        id?.let { EntityId.of(it) }
            ?.let { Category.of(it, name, Icon.valueOfOrDefault(icon)) }
            ?: Category.newOf(name, Icon.valueOfOrDefault(icon))
}
