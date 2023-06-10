package com.smtm.application.spring.resources

import com.smtm.application.HalCollection
import com.smtm.application.Link
import com.smtm.application.MediaType
import com.smtm.application.domain.Icon
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.Violation
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.categories.newCategoryOf
import com.smtm.application.service.CategoriesService
import com.smtm.application.spring.exceptions.ApiProblemException
import com.smtm.application.spring.exceptions.ConstraintViolationsException
import com.smtm.application.v1.CategoriesApi
import com.smtm.application.v1.CategoryDto
import com.smtm.application.v1.NewCategoryDto
import com.smtm.application.v1.ApiProblemDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories")
class CategoriesResource(
    private val categoriesService: CategoriesService,
    private val ownerIdProvider: () -> OwnerId
) : CategoriesApi {

    private val collectionLinks = mapOf(
        "self" to Link("http://localhost:8080/categories")
    )

    @GetMapping(
        produces = [MediaType.VERSION_1_JSON]
    )
    override fun getAll(): HalCollection<CategoryDto> {
        return ownerIdProvider()
            .let { categoriesService.getAll(it) }
            .fold(CategoriesProblemHandler::handle, this::createDto)
    }

    @PostMapping(
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    override fun save(@RequestBody category: NewCategoryDto): CategoryDto {
        return category
            .toDomain()
            .let { categoriesService.create(it, ownerIdProvider()) }
            .fold(CategoriesProblemHandler::handle, this::createDto)
    }

    private fun createDto(categories: List<Category>) = categories
        .map { createDto(it) }
        .let { HalCollection(collectionLinks, it.size, it.size, mapOf("categories" to it)) }

    private fun createDto(category: Category) = CategoryDto(
        links = mapOf(
            "self" to Link("http://localhost:8080/categories/${category.id!!}")
        ),
        id = category.id!!,
        name = category.name,
        icon = category.icon.name
    )

    private fun NewCategoryDto.toDomain() = newCategoryOf(
        name = name,
        icon = Icon.valueOfOrNull(icon) ?: Icon.FOLDER
    )
}

private object CategoriesProblemHandler {

    fun handle(problem: CategoriesProblem): Nothing {
        when (problem) {
            is CategoriesProblem.Violations -> throw problem.toConstraintViolationsException()
            is CategoriesProblem.Other -> throw ApiProblemException(ApiProblemDto.Undefined())
        }
    }

    private fun CategoriesProblem.Violations.toConstraintViolationsException() = violations
        .map { it.toDto() }
        .let { ApiProblemDto.ConstraintViolations(it) }
        .let { ConstraintViolationsException(it) }

    private fun Violation.toDto() = ApiProblemDto.Violation(
        path = path.toJsonPath(),
        code = code.name,
        message = code.name,
        parameters = emptyMap()
    )
}