package com.smtm.application.transactions.categories.v1

import com.smtm.application.common.extensions.toMediaType
import com.smtm.transactions.api.Category
import com.smtm.transactions.api.categoryOf
import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity

data class CategoryDto(val id: Long?, val name: String, val icon: String) : RepresentationModel<CategoryDto>() {

    fun toResponse201(): ResponseEntity<CategoryDto> = ResponseEntity.created(getRequiredLink("self").toUri())
        .contentType(MediaTypeValue.toMediaType())
        .body(this)

    fun toCategory(): Category = categoryOf(id, name, icon)

    companion object {

        const val MediaTypeValue = "application/smtm.category.v1+json"
    }
}

private val Category.selfLink: Link?
    get() = id
        ?.let { linkTo(methodOn(CategoriesController::class.java).get(it)).withSelfRel() }

fun Category.toDto(): CategoryDto = selfLink
    ?.let { CategoryDto(id, name, icon).add(it) }
    ?: CategoryDto(id, name, icon)
