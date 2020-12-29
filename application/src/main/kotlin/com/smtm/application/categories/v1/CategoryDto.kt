package com.smtm.application.categories.v1

import com.smtm.application.common.extensions.toMediaType
import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity

data class CategoryDto(val id: Long?, val name: String, val icon: String) : RepresentationModel<CategoryDto>() {

    fun toResponse201() = ResponseEntity.created(getRequiredLink("self").toUri())
        .contentType(MediaTypeValue.toMediaType())
        .body(this)
    
    companion object {

        const val MediaTypeValue = "application/smtm.category.v1+json"
    }
}

private val Category.selfLink: Link
    get() = linkTo(methodOn(CategoriesController::class.java).get(id)).withSelfRel()

fun categoryDtoOf(category: Category): CategoryDto = CategoryDto(category.id, category.name, category.icon)
    .add(category.selfLink)
