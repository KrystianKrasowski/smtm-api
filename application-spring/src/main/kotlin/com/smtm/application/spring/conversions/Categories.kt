package com.smtm.application.spring.conversions

import com.smtm.api.LinkFactory
import com.smtm.core.domain.Icon
import com.smtm.core.domain.categories.Category
import com.smtm.application.spring.endpoints.CategoriesEndpoint
import com.smtm.api.v1.CategoryDto
import com.smtm.api.v1.CategoryResource

object Categories {

    fun CategoryDto.toDomain(id: Long? = null): Category =
        Category.of(
            id = id,
            name = name,
            icon = Icon.valueOfOrNull(icon) ?: Icon.FOLDER
        )

    fun Category.toDto(linkFactory: LinkFactory): CategoryResource =
        CategoryResource(
            links = mapOf(
                "self" to linkFactory.create("${CategoriesEndpoint.PATH}/${id.value}")
            ),
            id = id.value,
            category = CategoryDto(
                name = name,
                icon = icon.name
            )
        )
}
