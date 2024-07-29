package com.smtm.application.spring.resources

import arrow.core.getOrElse
import com.smtm.application.HalCollection
import com.smtm.application.LinkFactory
import com.smtm.application.MediaType
import com.smtm.application.api.CategoriesApi
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import com.smtm.application.spring.conversions.Categories.toDomain
import com.smtm.application.spring.conversions.Categories.toDto
import com.smtm.application.spring.conversions.Violations.toDto
import com.smtm.application.v1.ApiProblemDto
import com.smtm.application.v1.CategoryDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(CategoriesResource.PATH)
class CategoriesResource(
    private val linkFactory: LinkFactory,
    private val categoriesService: CategoriesApi,
    private val ownerIdProvider: () -> OwnerId
) {

    private val collectionLinks = mapOf(
        "self" to linkFactory.create(PATH)
    )

    @GetMapping(
        produces = [MediaType.VERSION_1_JSON]
    )
    fun getAll(): ResponseEntity<*> {
        return ownerIdProvider()
            .let { categoriesService.getAll(it) }
            .map { create200Response(it.current) }
            .getOrElse(CategoriesProblemHandler::handle)
    }

    @PostMapping(
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun save(@RequestBody category: CategoryDto): ResponseEntity<*> {
        return category
            .toDomain()
            .let { categoriesService.save(it, ownerIdProvider()) }
            .map { it.getByName(category.name) }
            .map { create201Response(it) }
            .getOrElse(CategoriesProblemHandler::handle)
    }

    @PutMapping(
        path = ["/{id}"],
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun update(@PathVariable("id") id: Long, @RequestBody category: CategoryDto): ResponseEntity<*> {
        return category
            .toDomain(id)
            .let { categoriesService.save(it, ownerIdProvider()) }
            .map { it.getById(NumericId.of(id)) }
            .map { create200Response(it) }
            .getOrElse(CategoriesProblemHandler::handle)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("id") id: Long): ResponseEntity<*> {
        return categoriesService
            .delete(id, ownerIdProvider())
            .map { create204Response() }
            .getOrElse(CategoriesProblemHandler::handle)
    }

    private fun create200Response(categories: List<Category>) = categories
        .map { it.toDto(linkFactory) }
        .let { HalCollection(collectionLinks, it.size, it.size, mapOf("categories" to it)) }
        .let { ResponseEntity.ok(it) }

    private fun create200Response(category: Category) =
        category
            .toDto(linkFactory)
            .toResponse2xx(200)

    private fun create201Response(category: Category) =
        category
            .toDto(linkFactory)
            .toResponse2xx(201)

    private fun create204Response() = ResponseEntity.status(204)
        .build<Nothing>()

    private fun CategoryDto.toResponse2xx(status: Int) = ResponseEntity.status(status)
        .header("Location", links["self"]?.href)
        .body(this)

    companion object {

        const val PATH = "/categories"
    }
}

private object CategoriesProblemHandler {

    fun handle(problem: CategoriesProblem): ResponseEntity<*> {
        return when (problem) {
            is CategoriesProblem.Violations -> problem.violations
                .map { it.toDto() }
                .let { ApiProblemDto.ConstraintViolations(it) }
                .let {
                    ResponseEntity.status(422)
                        .header("Content-Type", "application/problem+json")
                        .body(it)
                }

            is CategoriesProblem.Unknown -> ApiProblemDto.UnknownResource()
                .let { ResponseEntity.status(404).body(it) }

            is CategoriesProblem.Other -> ApiProblemDto.Undefined()
                .let { ResponseEntity.status(500).body(it) }
        }
    }
}
