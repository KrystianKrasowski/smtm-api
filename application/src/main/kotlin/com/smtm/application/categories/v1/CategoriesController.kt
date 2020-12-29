package com.smtm.application.categories.v1

import com.smtm.application.common.extensions.toMediaType
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping(path = ["/categories"])
class CategoriesController {

    @GetMapping(path = ["/{id}"])
    fun get(@PathVariable id: Long): ResponseEntity<*> {
        TODO("Not implemented yet")
    }

    @PostMapping(
            consumes = [CategoryDto.MediaTypeValue],
            produces = [CategoryDto.MediaTypeValue]
    )
    fun create(@RequestBody category: CategoryDto): ResponseEntity<*> = categoryOf(1, category.name, category.icon)
            .toResponse200()
}

private val Category.selfLink
    get() = linkTo(methodOn(CategoriesController::class.java).get(id))

private fun Category.toResponse200() = categoryDtoOf(this)
        .toEntityModel(selfLink.withSelfRel())
        .toResponseEntity(selfLink.toUri())

private fun CategoryDto.toEntityModel(selfLink: Link) = EntityModel.of(this).add(selfLink)

private fun EntityModel<CategoryDto>.toResponseEntity(location: URI) = ResponseEntity.created(location)
        .contentType(CategoryDto.MediaTypeValue.toMediaType())
        .body(this)

