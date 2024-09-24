package com.smtm.application.spring.endpoints

import arrow.core.getOrElse
import com.smtm.api.LinkFactory
import com.smtm.api.MediaType
import com.smtm.api.ResourcePaths
import com.smtm.api.v1.ApiProblemDto
import com.smtm.api.v1.CategoriesCollection
import com.smtm.api.v1.CategoryDto
import com.smtm.api.v1.CategoryResource
import com.smtm.application.spring.conversions.Categories.toDomain
import com.smtm.application.spring.conversions.Categories.toHalCollection
import com.smtm.application.spring.conversions.Categories.toResource
import com.smtm.application.spring.endpoints.exceptions.CategoriesProblemException
import com.smtm.core.api.CategoriesApi
import com.smtm.core.domain.EntityId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
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
    fun getAll(): ResponseEntity<CategoriesCollection> =
        categoriesApi.getAll()
            .map { it.toHalCollection(linkFactory) }
            .map { ResponseEntity.ok(it) }
            .getOrElse { throw CategoriesProblemException(it) }

    @PostMapping(
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun create(@RequestBody categoryDto: CategoryDto): ResponseEntity<CategoryResource> =
        categoriesApi
            .create(categoryDto.toDomain())
            .map { it.getByName(categoryDto.name) }
            .map { it.toResource(linkFactory) }
            .map { ResponseEntity.created(it.getSelfURI()).body(it) }
            .getOrElse { throw CategoriesProblemException(it) }

    @PutMapping(
        path = ["/{id}"],
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun update(
        @PathVariable("id") id: String,
        @RequestBody categoryDto: CategoryDto
    ): ResponseEntity<CategoryResource> =
        categoriesApi
            .update(categoryDto.toDomain(id))
            .map { it.getByName(categoryDto.name) }
            .map { it.toResource(linkFactory) }
            .map { ResponseEntity.ok(it) }
            .getOrElse { throw CategoriesProblemException(it) }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ResponseEntity<Nothing> =
        categoriesApi
            .delete(EntityId.of(id))
            .map { ResponseEntity.status(HttpStatus.NO_CONTENT).build<Nothing>() }
            .getOrElse { throw CategoriesProblemException(it) }

    @ExceptionHandler
    fun handleCategoriesProblemException(exception: CategoriesProblemException): ResponseEntity<ApiProblemDto> =
        exception.toResponseEntity()
}
