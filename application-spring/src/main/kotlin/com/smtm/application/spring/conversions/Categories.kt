package com.smtm.application.spring.conversions

import com.smtm.application.LinkFactory
import com.smtm.application.domain.Icon
import com.smtm.application.domain.categories.Category
import com.smtm.application.spring.resources.CategoriesResource
import com.smtm.application.v1.CategoryDto

object Categories {

    fun CategoryDto.toDomain(id: Long? = null): Category =
        Category.of(
            id = this.id ?: id,
            name = name,
            icon = Icon.valueOfOrNull(icon) ?: Icon.FOLDER
        )

    fun Category.toDto(linkFactory: LinkFactory): CategoryDto =
        CategoryDto(
            links = mapOf(
                "self" to linkFactory.create("${CategoriesResource.PATH}/${id.value}")
            ),
            id = id.value,
            name = name,
            icon = icon.name
        )
}
