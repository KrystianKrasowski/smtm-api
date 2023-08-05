package com.smtm.application.spring.resources

import com.smtm.application.LinkFactory
import com.smtm.application.domain.Violation
import com.smtm.application.domain.categories.Category
import com.smtm.application.v1.ApiProblemDto
import com.smtm.application.v1.CategoryDto

class DtoFactory(private val linkFactory: LinkFactory) {

    fun create(category: Category): CategoryDto =
        CategoryDto(
            links = mapOf(
                "self" to linkFactory.create("${CategoriesResource.PATH}/${category.id.value}")
            ),
            id = category.id.value,
            name = category.name,
            icon = category.icon.name
        )

    companion object {

        fun create(violation: Violation): ApiProblemDto.Violation =
            ApiProblemDto.Violation(
                path = violation.path.toJsonPath(),
                code = violation.code.name,
                message = violation.code.name,
                parameters = violation.parameters
            )
    }
}