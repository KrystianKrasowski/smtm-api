package com.smtm.application.spring.endpoints

import arrow.core.getOrElse
import com.smtm.api.LinkFactory
import com.smtm.api.MediaType
import com.smtm.api.ResourcePaths
import com.smtm.api.v1.ApiProblemDto
import com.smtm.api.v1.CategoryDto
import com.smtm.application.spring.conversions.CategoriesConversions.toHalCollection
import com.smtm.application.spring.conversions.CategoriesConversions.toResource
import com.smtm.application.spring.conversions.Violations.toDto
import com.smtm.core.api.CategoriesApi
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.domain.categories.Category
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ResourcePaths.CATEGORIES)
class CategoriesEndpoint(
    private val categoriesApi: CategoriesApi,
    private val linkFactory: LinkFactory
) {

    @GetMapping(
        produces = [MediaType.VERSION_1_JSON]
    )
    fun getAll(): ResponseEntity<*> =
        categoriesApi.getAll()
            .map { it.toHalCollection(linkFactory) }
            .map { ResponseEntity.ok(it) }
            .getOrElse { handleProblem(it) }

    @PostMapping(
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun create(@RequestBody categoryDto: CategoryDto): ResponseEntity<*> =
        categoriesApi
            .create(categoryDto.toDomain())
            .map { it.getByName(categoryDto.name) }
            .map { it.toResource(linkFactory) }
            .map { ResponseEntity.created(it.getSelfURI()).body(it) }
            .getOrElse { handleProblem(it) }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String, @RequestBody categoryDto: CategoryDto): ResponseEntity<*> =
        categoriesApi
            .update(categoryDto.toDomain(id))
            .map { it.getByName(categoryDto.name) }
            .map { it.toResource(linkFactory) }
            .map { ResponseEntity.ok(it) }
            .getOrElse { handleProblem(it) }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ResponseEntity<*> =
        categoriesApi
            .delete(EntityId.of(id))
            .map { ResponseEntity.status(HttpStatus.NO_CONTENT).build<Nothing>() }
            .getOrElse { handleProblem(it) }
}

private fun CategoryDto.toDomain(id: String? = null): Category =
    id?.let { EntityId.of(it) }
        ?.let { Category.of(it, name, Icon.valueOfOrDefault(icon)) }
        ?: Category.newOf(name, Icon.valueOfOrDefault(icon))

private fun handleProblem(problem: CategoriesProblem): ResponseEntity<*> =
    when (problem) {
        is CategoriesProblem.ValidationErrors -> ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .header("Content-Type", MediaType.PROBLEM)
            .body(ApiProblemDto.ConstraintViolations(problem.violations.map { it.toDto() }))

        is CategoriesProblem.Unknown -> ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .header("Content-Type", MediaType.PROBLEM)
            .body(ApiProblemDto.UnknownResource())

        is CategoriesProblem.Failure -> ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .header("Content-Type", MediaType.PROBLEM)
            .body(ApiProblemDto.Undefined())
    }
